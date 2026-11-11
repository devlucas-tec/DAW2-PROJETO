package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.ConflitoException;
import br.edu.ifpb.es.daw.exception.EstadoInvalidoException;
import br.edu.ifpb.es.daw.model.Entrega;
import br.edu.ifpb.es.daw.model.Pagamento;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.EtapaPedido;
import br.edu.ifpb.es.daw.model.enums.StatusEntrega;
import br.edu.ifpb.es.daw.model.enums.StatusPagamento;
import br.edu.ifpb.es.daw.repository.EntregaRepository;
import br.edu.ifpb.es.daw.repository.PagamentoRepository;
import br.edu.ifpb.es.daw.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * Transições que o VENDEDOR dono dos itens (ou o ADMIN) faz no pedido:
 * confirmar pagamento → EMBALADO → EM_TRANSITO → SAIU_PARA_ENTREGA → ENTREGUE.
 * Cada transição precisa ser exatamente a próxima etapa e fica registrada no histórico.
 */
@Service
public class CicloPedidoService {

    public static final Set<EtapaPedido> ETAPAS_DO_VENDEDOR = Set.of(
            EtapaPedido.PAGAMENTO_APROVADO,
            EtapaPedido.EMBALADO,
            EtapaPedido.EM_TRANSITO,
            EtapaPedido.SAIU_PARA_ENTREGA,
            EtapaPedido.ENTREGUE
    );

    @Autowired private PedidoService pedidoService;
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private PagamentoRepository pagamentoRepository;
    @Autowired private EntregaRepository entregaRepository;
    @Autowired private PedidoHistoricoService historicoService;
    @Autowired private UsuarioAutenticadoService usuarioAutenticado;

    @Transactional
    public Pedido avancar(Long idPedido, EtapaPedido destino, String transportadora, String codigoRastreamento) {
        Pedido pedido = pedidoService.buscarPorId(idPedido);
        usuarioAutenticado.validarVendedorDoPedido(pedido);

        if (destino == null || !ETAPAS_DO_VENDEDOR.contains(destino)) {
            throw new EstadoInvalidoException("Etapa '" + destino + "' não pode ser definida pelo vendedor. "
                    + "Etapas aceitas: PAGAMENTO_APROVADO, EMBALADO, EM_TRANSITO, SAIU_PARA_ENTREGA, ENTREGUE.");
        }

        EtapaPedido esperada = pedido.getProximaEtapa();
        if (esperada == null) {
            throw new EstadoInvalidoException(motivoSemProximaEtapa(pedido));
        }
        if (destino != esperada) {
            throw new EstadoInvalidoException("Transição inválida: " + pedido.getEtapaAtual() + " → " + destino
                    + ". A próxima etapa permitida é " + esperada + ".");
        }

        String descricao = switch (destino) {
            case PAGAMENTO_APROVADO -> aprovarPagamento(pedido);
            case EMBALADO -> embalar(pedido, transportadora, codigoRastreamento);
            case EM_TRANSITO -> despachar(pedido, transportadora, codigoRastreamento);
            case SAIU_PARA_ENTREGA -> {
                pedido.getEntrega().setStatusEntrega(StatusEntrega.SAIU_PARA_ENTREGA);
                yield "Pedido saiu para entrega.";
            }
            case ENTREGUE -> {
                pedido.getEntrega().setStatusEntrega(StatusEntrega.ENTREGUE);
                pedido.getEntrega().setDataEntrega(LocalDateTime.now());
                yield "Pedido entregue.";
            }
            default -> throw new IllegalStateException("Etapa não tratada: " + destino);
        };

        historicoService.registrar(pedido, destino, descricao);
        return pedidoRepository.save(pedido);
    }

    private String aprovarPagamento(Pedido pedido) {
        Pagamento pagamento = pedido.getPagamento();
        pagamento.setStatus(StatusPagamento.APROVADO);
        pagamentoRepository.save(pagamento);
        return "Pagamento via " + pagamento.getMetodo() + " confirmado.";
    }

    private String embalar(Pedido pedido, String transportadora, String codigoRastreamento) {
        validarCodigoDisponivel(codigoRastreamento, null);
        Entrega entrega = new Entrega();
        entrega.setPedido(pedido);
        entrega.setTransportadora(transportadora);
        entrega.setCodigoRastreamento(codigoRastreamento);
        entrega.setStatusEntrega(StatusEntrega.EMBALADO);
        pedido.setEntrega(entregaRepository.save(entrega));
        return "Pedido embalado e aguardando envio.";
    }

    private String despachar(Pedido pedido, String transportadora, String codigoRastreamento) {
        Entrega entrega = pedido.getEntrega();
        if (transportadora != null && !transportadora.isBlank()) {
            entrega.setTransportadora(transportadora);
        }
        if (codigoRastreamento != null && !codigoRastreamento.isBlank()) {
            validarCodigoDisponivel(codigoRastreamento, entrega.getId());
            entrega.setCodigoRastreamento(codigoRastreamento);
        }
        if (isVazio(entrega.getTransportadora()) || isVazio(entrega.getCodigoRastreamento())) {
            throw new EstadoInvalidoException(
                    "Para marcar o pedido como EM_TRANSITO informe a transportadora e o código de rastreamento.");
        }
        LocalDateTime agora = LocalDateTime.now();
        entrega.setStatusEntrega(StatusEntrega.EM_TRANSITO);
        entrega.setDataEnvio(agora);
        entrega.setDataEntregaPrevista(agora.plusDays(7));
        return "Pedido enviado pela " + entrega.getTransportadora()
                + ". Código de rastreamento: " + entrega.getCodigoRastreamento() + ".";
    }

    private void validarCodigoDisponivel(String codigoRastreamento, Long idEntregaAtual) {
        if (isVazio(codigoRastreamento)) {
            return;
        }
        entregaRepository.findByCodigoRastreamento(codigoRastreamento)
                .filter(existente -> !Objects.equals(existente.getId(), idEntregaAtual))
                .ifPresent(existente -> {
                    throw new ConflitoException("Já existe uma entrega com o código de rastreamento '"
                            + codigoRastreamento + "'.");
                });
    }

    private String motivoSemProximaEtapa(Pedido pedido) {
        if (pedido.getStatus() != br.edu.ifpb.es.daw.model.enums.StatusPedido.CONFIRMADO) {
            return "O pedido está " + pedido.getStatus() + " e não pode mais avançar.";
        }
        if (pedido.getPagamento() == null) {
            return "O pedido ainda não tem pagamento. Aguarde o cliente pagar.";
        }
        if (pedido.getPagamento().getStatus() != StatusPagamento.APROVADO
                && pedido.getPagamento().getStatus() != StatusPagamento.PENDENTE) {
            return "O pagamento do pedido está " + pedido.getPagamento().getStatus() + ".";
        }
        return "O pedido já foi entregue.";
    }

    private boolean isVazio(String texto) {
        return texto == null || texto.isBlank();
    }
}
