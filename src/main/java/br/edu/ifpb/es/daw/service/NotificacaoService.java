package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.model.ItemPedido;
import br.edu.ifpb.es.daw.model.Notificacao;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.Usuario;
import br.edu.ifpb.es.daw.model.Vendedor;
import br.edu.ifpb.es.daw.model.enums.EtapaPedido;
import br.edu.ifpb.es.daw.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Notificações dos usuários. São geradas a cada etapa registrada na timeline do pedido:
 * o vendedor é avisado de novos pedidos, cancelamentos e devoluções; o cliente, de toda mudança de status.
 * Quem fez a ação não recebe aviso dela mesma.
 */
@Service
public class NotificacaoService {

    private static final int PAGE_SIZE = 10;

    /** Etapas que geram aviso para o vendedor dono dos itens do pedido. */
    private static final Map<EtapaPedido, String> TITULOS_VENDEDOR = new EnumMap<>(Map.of(
            EtapaPedido.PEDIDO_CONFIRMADO, "Novo pedido",
            EtapaPedido.CANCELADO, "Pedido cancelado",
            EtapaPedido.DEVOLUCAO_SOLICITADA, "Devolução solicitada"
    ));

    private static final Map<EtapaPedido, String> TITULOS_CLIENTE = new EnumMap<>(EtapaPedido.class);

    static {
        TITULOS_CLIENTE.put(EtapaPedido.PAGAMENTO_APROVADO, "Pagamento aprovado");
        TITULOS_CLIENTE.put(EtapaPedido.PAGAMENTO_RECUSADO, "Pagamento recusado");
        TITULOS_CLIENTE.put(EtapaPedido.EMBALADO, "Pedido embalado");
        TITULOS_CLIENTE.put(EtapaPedido.EM_TRANSITO, "Pedido a caminho");
        TITULOS_CLIENTE.put(EtapaPedido.SAIU_PARA_ENTREGA, "Saiu para entrega");
        TITULOS_CLIENTE.put(EtapaPedido.ENTREGUE, "Pedido entregue");
        TITULOS_CLIENTE.put(EtapaPedido.CANCELADO, "Pedido cancelado");
        TITULOS_CLIENTE.put(EtapaPedido.DEVOLUCAO_RECUSADA, "Devolução recusada");
        TITULOS_CLIENTE.put(EtapaPedido.DEVOLVIDO, "Devolução aprovada");
    }

    @Autowired private NotificacaoRepository repository;
    @Autowired private UsuarioAutenticadoService usuarioAutenticado;

    @Transactional
    public Notificacao notificar(Usuario destinatario, String titulo, String mensagem, String link, Long idPedido) {
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(destinatario);
        notificacao.setTitulo(limitar(titulo, 100));
        notificacao.setMensagem(limitar(mensagem, 300));
        notificacao.setLink(link);
        notificacao.setIdPedido(idPedido);
        return repository.save(notificacao);
    }

    /** Chamado pelo {@link PedidoHistoricoService} a cada nova etapa do pedido. */
    @Transactional
    public void notificarEtapa(Pedido pedido, EtapaPedido etapa, String descricao) {
        Long idAutor = usuarioAutenticado.getUsuarioOpcional().map(Usuario::getId).orElse(null);
        String detalhe = descricao == null || descricao.isBlank() ? "" : " " + descricao;

        String tituloVendedor = TITULOS_VENDEDOR.get(etapa);
        if (tituloVendedor != null) {
            for (Vendedor vendedor : vendedoresDoPedido(pedido).values()) {
                if (!Objects.equals(vendedor.getId(), idAutor)) {
                    String mensagem = etapa == EtapaPedido.PEDIDO_CONFIRMADO
                            ? pedido.getCliente().getNome() + " fez o pedido #" + pedido.getId() + "."
                            : "Pedido #" + pedido.getId() + "." + detalhe;
                    notificar(vendedor, tituloVendedor + " #" + pedido.getId(), mensagem,
                            etapa == EtapaPedido.DEVOLUCAO_SOLICITADA ? "/vendedor/devolucoes" : "/vendedor/pedidos/" + pedido.getId(),
                            pedido.getId());
                }
            }
        }

        String tituloCliente = TITULOS_CLIENTE.get(etapa);
        if (tituloCliente != null && pedido.getCliente() != null
                && !Objects.equals(pedido.getCliente().getId(), idAutor)) {
            notificar(pedido.getCliente(), tituloCliente + " #" + pedido.getId(),
                    "Pedido #" + pedido.getId() + "." + detalhe, "/conta/pedidos/" + pedido.getId(), pedido.getId());
        }
    }

    public Page<Notificacao> minhas(boolean somenteNaoLidas, int page) {
        return repository.buscarDoUsuario(idLogado(), somenteNaoLidas, PageRequest.of(Math.max(page, 0), PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, "dataCriacao").and(Sort.by(Sort.Direction.DESC, "id"))));
    }

    public long contarNaoLidas() {
        return repository.countByUsuarioIdAndLidaFalse(idLogado());
    }

    @Transactional
    public Notificacao marcarComoLida(Long id) {
        Notificacao notificacao = repository.findById(id)
                .filter(n -> Objects.equals(n.getUsuario().getId(), idLogado()))
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Notificacao", id));
        notificacao.setLida(true);
        return repository.save(notificacao);
    }

    @Transactional
    public int marcarTodasComoLidas() {
        return repository.marcarTodasComoLidas(idLogado());
    }

    @Transactional
    public void removerDoUsuario(Long idUsuario) {
        repository.removerDoUsuario(idUsuario);
    }

    private Map<Long, Vendedor> vendedoresDoPedido(Pedido pedido) {
        Map<Long, Vendedor> vendedores = new LinkedHashMap<>();
        for (ItemPedido item : pedido.getItens()) {
            Optional.ofNullable(item.getProduto()).map(p -> p.getVendedor())
                    .ifPresent(v -> vendedores.putIfAbsent(v.getId(), v));
        }
        return vendedores;
    }

    private Long idLogado() {
        return usuarioAutenticado.getUsuario().getId();
    }

    private static String limitar(String texto, int maximo) {
        return texto == null || texto.length() <= maximo ? texto : texto.substring(0, maximo - 1) + "…";
    }
}
