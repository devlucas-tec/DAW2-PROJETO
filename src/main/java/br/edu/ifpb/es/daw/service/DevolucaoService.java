package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.ConflitoException;
import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.exception.EstadoInvalidoException;
import br.edu.ifpb.es.daw.model.Devolucao;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.StatusDevolucao;
import br.edu.ifpb.es.daw.model.enums.StatusEntrega;
import br.edu.ifpb.es.daw.model.enums.StatusPagamento;
import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import br.edu.ifpb.es.daw.repository.DevolucaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DevolucaoService {

    @Autowired
    private DevolucaoRepository repository;

    @Autowired
    private PedidoService pedidoService;

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
        return repository.save(devolucao);
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
        return repository.save(devolucao);
    }

    @Transactional
    public Devolucao rejeitar(Long id) {
        Devolucao devolucao = buscarPorId(id);
        validarPendente(devolucao, "rejeitada");
        devolucao.setStatus(StatusDevolucao.RECUSADA);
        return repository.save(devolucao);
    }

    private void validarPendente(Devolucao devolucao, String operacao) {
        if (devolucao.getStatus() != StatusDevolucao.PENDENTE) {
            throw new EstadoInvalidoException("Devolução só pode ser " + operacao
                    + " se estiver PENDENTE. Status atual: " + devolucao.getStatus());
        }
    }
}
