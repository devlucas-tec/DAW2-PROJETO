package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Avaliacao;
import br.edu.ifpb.es.daw.repository.AvaliacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository repository;

    @Transactional
    public Avaliacao criar(Avaliacao obj) {

        return repository.save(obj);
    }

    public List<Avaliacao> recuperarTodos() {

        return repository.findAll();
    }

    public Optional<Avaliacao> buscarPorId(Long id) {

        return repository.findById(id);
    }

    @Transactional
    public void remover(Avaliacao obj) {

        repository.delete(obj);
    }
}
