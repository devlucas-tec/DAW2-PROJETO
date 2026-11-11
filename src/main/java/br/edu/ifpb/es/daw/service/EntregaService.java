package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.model.Entrega;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.EtapaPedido;
import br.edu.ifpb.es.daw.model.enums.StatusEntrega;
import br.edu.ifpb.es.daw.repository.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Mantém os endpoints de /entregas, delegando as regras de transição ao {@link CicloPedidoService}. */
@Service
public class EntregaService {

    @Autowired
    private EntregaRepository repository;

    @Autowired
    private CicloPedidoService cicloPedidoService;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticado;

    @Transactional
    public Entrega criar(Long idPedido, String transportadora, String codigoRastreamento) {
        Pedido pedido = cicloPedidoService.avancar(idPedido, EtapaPedido.EMBALADO, transportadora, codigoRastreamento);
        return pedido.getEntrega();
    }

    public Entrega buscarPorId(Long id) {
        Entrega entrega = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Entrega", id));
        usuarioAutenticado.validarAcessoPedido(entrega.getPedido());
        return entrega;
    }

    @Transactional
    public Entrega atualizarStatus(Long id, StatusEntrega novoStatus, String transportadora, String codigoRastreamento) {
        Entrega entrega = buscarPorId(id);
        Pedido pedido = cicloPedidoService.avancar(entrega.getPedido().getId(), EtapaPedido.de(novoStatus),
                transportadora, codigoRastreamento);
        return pedido.getEntrega();
    }
}
