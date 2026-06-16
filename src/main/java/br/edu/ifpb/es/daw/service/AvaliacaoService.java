package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Avaliacao;
import br.edu.ifpb.es.daw.repository.AvaliacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AvaliacaoService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private AvaliacaoRepository repository;

    @Transactional
    public Avaliacao criar(Avaliacao obj) {
        return repository.save(obj);
    }

    public Page<Avaliacao> recuperarTodos(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return repository.findAll(pageable);
    }

    public Optional<Avaliacao> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public void remover(Avaliacao obj) {
        repository.delete(obj);
    }
}
