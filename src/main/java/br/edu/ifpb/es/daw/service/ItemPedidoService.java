package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.ItemPedido;
import br.edu.ifpb.es.daw.repository.ItemPedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemPedidoService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private ItemPedidoRepository repository;

    @Transactional
    public ItemPedido criar(ItemPedido obj) {
        return repository.save(obj);
    }

    public Page<ItemPedido> recuperarTodos(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return repository.findAll(pageable);
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
