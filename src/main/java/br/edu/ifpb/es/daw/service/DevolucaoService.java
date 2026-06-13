package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Devolucao;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.StatusDevolucao;
import br.edu.ifpb.es.daw.repository.DevolucaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DevolucaoService {

    @Autowired
    private DevolucaoRepository repository;

    @Transactional
    public Devolucao criar(Devolucao obj) {

        return repository.save(obj);
    }

    public Optional<Devolucao> buscarPorId(Long id) {

        return repository.findById(id);
    }

    public Optional<Devolucao> buscarPorPedido(Pedido pedido) {
        return repository.buscarPorPedido(pedido);
    }

    @Transactional
    public Devolucao aprovar(Devolucao obj) {
        repository.atualizarStatus(obj.getId(), StatusDevolucao.APROVADA);
        return repository.findById(obj.getId()).get();
    }

    @Transactional
    public Devolucao rejeitar(Devolucao obj) {
        repository.atualizarStatus(obj.getId(), StatusDevolucao.RECUSADA);
        return repository.findById(obj.getId()).get();
    }
}
