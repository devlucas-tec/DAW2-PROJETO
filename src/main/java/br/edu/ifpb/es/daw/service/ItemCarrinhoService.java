package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.ItemCarrinho;
import br.edu.ifpb.es.daw.repository.ItemCarrinhoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemCarrinhoService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private ItemCarrinhoRepository repository;

    @Transactional
    public ItemCarrinho criar(ItemCarrinho obj) {
        return repository.save(obj);
    }

    public Page<ItemCarrinho> recuperarTodos(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return repository.findAll(pageable);
    }

    public Optional<ItemCarrinho> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ItemCarrinho atualizar(ItemCarrinho obj) {
        return repository.save(obj);
    }

    @Transactional
    public void remover(ItemCarrinho obj) {
        repository.delete(obj);
    }
}
