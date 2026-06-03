package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.ItemPedido;
import br.edu.ifpb.es.daw.repository.ItemPedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemPedidoService {
    @Autowired
    private ItemPedidoRepository repository;

    @Transactional
    public ItemPedido criar(ItemPedido obj) {

        return repository.save(obj);
    }

    public List<ItemPedido> recuperarTodos() {

        return repository.findAll();
    }

    public Optional<ItemPedido> buscarPorId(Long id) {

        return repository.findById(id);
    }

    @Transactional
    public ItemPedido atualizar(ItemPedido obj) {

        return repository.save(obj);
    }

    @Transactional
    public void remover(ItemPedido obj) {

        repository.delete(obj);
    }
}
