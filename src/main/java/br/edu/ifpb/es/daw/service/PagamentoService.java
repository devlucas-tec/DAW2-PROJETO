package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.ConflitoException;
import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.exception.EstadoInvalidoException;
import br.edu.ifpb.es.daw.model.Pagamento;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.EtapaPedido;
import br.edu.ifpb.es.daw.model.enums.MetodoPagamento;
import br.edu.ifpb.es.daw.model.enums.StatusPagamento;
import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import br.edu.ifpb.es.daw.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private CicloPedidoService cicloPedidoService;

    @Autowired
    private PedidoHistoricoService historicoService;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticado;

    @Transactional
    public Pagamento criar(Long idPedido, MetodoPagamento metodo) {
        Pedido pedido = pedidoService.buscarComoDono(idPedido);
        pedidoService.validarStatus(pedido, StatusPedido.CONFIRMADO, "pago");

        Pagamento pagamento = repository.findByPedido(pedido).orElse(null);
        if (pagamento != null && pagamento.getStatus() != StatusPagamento.RECUSADO) {
            throw new ConflitoException("O pedido já possui um pagamento " + pagamento.getStatus() + ".");
        }
        if (pagamento == null) {
            pagamento = new Pagamento();
            pagamento.setPedido(pedido);
        }
        pagamento.setMetodo(metodo);
        pagamento.setValorPago(pedido.getValorTotal());
        pagamento.setStatus(StatusPagamento.PENDENTE);
        pagamento.setDataPagamento(LocalDateTime.now());
        return repository.save(pagamento);
    }

    public Pagamento buscarPorId(Long id) {
        Pagamento pagamento = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pagamento", id));
        usuarioAutenticado.validarAcessoPedido(pagamento.getPedido());
        return pagamento;
    }

    /** Confirmação feita pelo vendedor dono dos itens (ou ADMIN); entra no histórico do pedido. */
    @Transactional
    public Pagamento confirmar(Long id) {
        Pagamento pagamento = buscarPorId(id);
        validarPendente(pagamento, "confirmado");
        return cicloPedidoService.avancar(pagamento.getPedido().getId(), EtapaPedido.PAGAMENTO_APROVADO, null, null)
                .getPagamento();
    }

    @Transactional
    public Pagamento cancelar(Long id) {
        Pagamento pagamento = buscarPorId(id);
        usuarioAutenticado.validarDonoPedido(pagamento.getPedido());
        validarPendente(pagamento, "cancelado");
        pagamento.setStatus(StatusPagamento.RECUSADO);
        historicoService.registrar(pagamento.getPedido(), EtapaPedido.PAGAMENTO_RECUSADO, "Pagamento cancelado pelo cliente.");
        return repository.save(pagamento);
    }

    private void validarPendente(Pagamento pagamento, String operacao) {
        if (pagamento.getStatus() != StatusPagamento.PENDENTE) {
            throw new EstadoInvalidoException("Só pagamentos PENDENTES podem ser " + operacao
                    + ". Status atual: " + pagamento.getStatus());
        }
    }
}
