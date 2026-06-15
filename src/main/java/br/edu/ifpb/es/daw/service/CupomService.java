package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Cupom;
import br.edu.ifpb.es.daw.repository.CupomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CupomService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private CupomRepository repository;

    @Transactional
    public Cupom criar(Cupom obj) {
        return repository.save(obj);
    }

    public Page<Cupom> recuperarTodos(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return repository.findAll(pageable);
    }

    public Optional<Cupom> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Optional<Cupom> buscarPorCodigo(String codigo) {
        return repository.buscarPorCodigo(codigo);
    }

    @Transactional
    public Cupom atualizar(Cupom obj) {
        return repository.save(obj);
    }

    @Transactional
    public void remover(Cupom obj) {
        repository.delete(obj);
    }
}
