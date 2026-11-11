package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.ConflitoException;
import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.exception.EstadoInvalidoException;
import br.edu.ifpb.es.daw.model.Devolucao;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.EtapaPedido;
import br.edu.ifpb.es.daw.model.enums.StatusDevolucao;
import br.edu.ifpb.es.daw.model.enums.StatusEntrega;
import br.edu.ifpb.es.daw.model.enums.StatusPagamento;
import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import br.edu.ifpb.es.daw.repository.DevolucaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DevolucaoService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private DevolucaoRepository repository;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoHistoricoService historicoService;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticado;

    @Transactional
    public Devolucao criar(Long idPedido, String motivo) {
        Pedido pedido = pedidoService.buscarComoDono(idPedido);
        pedidoService.validarStatus(pedido, StatusPedido.CONFIRMADO, "devolvido");
        if (pedido.getEntrega() == null || pedido.getEntrega().getStatusEntrega() != StatusEntrega.ENTREGUE) {
            throw new EstadoInvalidoException("Só é possível solicitar devolução de pedidos já ENTREGUES.");
        }
        if (repository.existsByPedidoAndStatusIn(pedido, List.of(StatusDevolucao.PENDENTE, StatusDevolucao.APROVADA))) {
            throw new ConflitoException("Já existe uma devolução pendente ou aprovada para este pedido.");
        }
        Devolucao devolucao = new Devolucao();
        devolucao.setPedido(pedido);
        devolucao.setMotivo(motivo);
        devolucao.setStatus(StatusDevolucao.PENDENTE);
        historicoService.registrar(pedido, EtapaPedido.DEVOLUCAO_SOLICITADA, "Devolução solicitada: " + resumir(motivo));
        return repository.save(devolucao);
    }

    public Page<Devolucao> recuperarMinhas(int page) {
        return repository.findByPedidoClienteId(usuarioAutenticado.getUsuario().getId(),
                PageRequest.of(Math.max(page, 0), PAGE_SIZE,
                        Sort.by(Sort.Direction.DESC, "dataDevolucao").and(Sort.by(Sort.Direction.DESC, "id"))));
    }

    public Devolucao buscarPorId(Long id) {
        Devolucao devolucao = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Devolucao", id));
        usuarioAutenticado.validarAcessoPedido(devolucao.getPedido());
        return devolucao;
    }

    @Transactional
    public Devolucao aprovar(Long id) {
        Devolucao devolucao = buscarPorId(id);
        validarPendente(devolucao, "aprovada");
        Pedido pedido = devolucao.getPedido();
        pedidoService.devolverEstoque(pedido);
        pedido.setStatus(StatusPedido.DEVOLVIDO);
        if (pedido.getPagamento() != null && pedido.getPagamento().getStatus() == StatusPagamento.APROVADO) {
            pedido.getPagamento().setStatus(StatusPagamento.ESTORNADO);
        }
        devolucao.setStatus(StatusDevolucao.APROVADA);
        historicoService.registrar(pedido, EtapaPedido.DEVOLVIDO, "Devolução aprovada e pagamento estornado.");
        return repository.save(devolucao);
    }

    @Transactional
    public Devolucao rejeitar(Long id) {
        Devolucao devolucao = buscarPorId(id);
        validarPendente(devolucao, "rejeitada");
        devolucao.setStatus(StatusDevolucao.RECUSADA);
        historicoService.registrar(devolucao.getPedido(), EtapaPedido.DEVOLUCAO_RECUSADA, "Devolução recusada.");
        return repository.save(devolucao);
    }

    private void validarPendente(Devolucao devolucao, String operacao) {
        if (devolucao.getStatus() != StatusDevolucao.PENDENTE) {
            throw new EstadoInvalidoException("Devolução só pode ser " + operacao
                    + " se estiver PENDENTE. Status atual: " + devolucao.getStatus());
        }
    }

    private String resumir(String texto) {
        return texto.length() <= 200 ? texto : texto.substring(0, 197) + "...";
    }
}
