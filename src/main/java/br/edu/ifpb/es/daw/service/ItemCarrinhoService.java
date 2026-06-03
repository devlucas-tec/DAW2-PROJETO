package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.ItemCarrinho;
import br.edu.ifpb.es.daw.repository.ItemCarrinhoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemCarrinhoService {

    @Autowired
    private ItemCarrinhoRepository repository;

    @Transactional
    public ItemCarrinho criar(ItemCarrinho obj) {

        return repository.save(obj);
    }

    public List<ItemCarrinho> recuperarTodos() {

        return repository.findAll();
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
