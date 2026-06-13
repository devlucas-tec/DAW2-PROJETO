package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Entrega;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.StatusEntrega;
import br.edu.ifpb.es.daw.repository.EntregaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository repository;

    @Transactional
    public Entrega criar(Entrega obj) {

        return repository.save(obj);
    }

    public Optional<Entrega> buscarPorId(Long id) {

        return repository.findById(id);
    }

    public Optional<Entrega> buscarPorPedido(Pedido pedido) {
        return repository.buscarPorPedido(pedido);
    }

    @Transactional
    public Entrega atualizarStatus(Entrega obj, StatusEntrega novoStatus) {
        repository.atualizarStatus(obj.getId(), novoStatus);
        return repository.findById(obj.getId()).get();
    }
}
