package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.ConflitoException;
import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.exception.EstadoInvalidoException;
import br.edu.ifpb.es.daw.model.Entrega;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.StatusEntrega;
import br.edu.ifpb.es.daw.model.enums.StatusPagamento;
import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import br.edu.ifpb.es.daw.repository.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository repository;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticado;

    @Transactional
    public Entrega criar(Long idPedido, String transportadora, String codigoRastreamento) {
        Pedido pedido = pedidoService.buscarComAcesso(idPedido);
        pedidoService.validarStatus(pedido, StatusPedido.CONFIRMADO, "enviado");
        if (pedido.getPagamento() == null || pedido.getPagamento().getStatus() != StatusPagamento.APROVADO) {
            throw new EstadoInvalidoException("O pedido só pode ser enviado após o pagamento ser APROVADO.");
        }
        if (repository.findByPedido(pedido).isPresent()) {
            throw new ConflitoException("O pedido já possui uma entrega registrada.");
        }
        if (codigoRastreamento != null && repository.existsByCodigoRastreamento(codigoRastreamento)) {
            throw new ConflitoException("Já existe uma entrega com o código de rastreamento '" + codigoRastreamento + "'.");
        }
        Entrega entrega = new Entrega();
        entrega.setPedido(pedido);
        entrega.setTransportadora(transportadora);
        entrega.setCodigoRastreamento(codigoRastreamento);
        entrega.setStatusEntrega(StatusEntrega.EMBALADO);
        return repository.save(entrega);
    }

    public Entrega buscarPorId(Long id) {
        Entrega entrega = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Entrega", id));
        usuarioAutenticado.validarAcessoPedido(entrega.getPedido());
        return entrega;
    }

    @Transactional
    public Entrega atualizarStatus(Long id, StatusEntrega novoStatus) {
        Entrega entrega = buscarPorId(id);
        StatusEntrega atual = entrega.getStatusEntrega();
        if (novoStatus.ordinal() <= atual.ordinal()) {
            throw new EstadoInvalidoException("O status da entrega só pode avançar. Status atual: " + atual
                    + ", status solicitado: " + novoStatus);
        }
        if (entrega.getPedido().getStatus() != StatusPedido.CONFIRMADO) {
            throw new EstadoInvalidoException("O pedido desta entrega não está CONFIRMADO.");
        }
        entrega.setStatusEntrega(novoStatus);
        if (novoStatus == StatusEntrega.ENTREGUE) {
            entrega.setDataEntrega(LocalDateTime.now());
        }
        return repository.save(entrega);
    }
}
