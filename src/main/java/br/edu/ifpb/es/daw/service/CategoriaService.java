package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Categoria;
import br.edu.ifpb.es.daw.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriaService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private CategoriaRepository repository;

    @Transactional
    public Categoria criar(Categoria obj) {
        return repository.save(obj);
    }

    public Page<Categoria> recuperarTodos(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return repository.findAll(pageable);
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Categoria atualizar(Categoria obj) {
        return repository.save(obj);
    }

    @Transactional
    public void remover(Categoria obj) {
        repository.delete(obj);
    }
}
