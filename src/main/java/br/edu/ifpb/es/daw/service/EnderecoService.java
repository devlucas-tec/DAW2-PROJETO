package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Endereco;
import br.edu.ifpb.es.daw.repository.EnderecoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnderecoService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private EnderecoRepository repository;

    @Transactional
    public Endereco criar(Endereco obj) {
        return repository.save(obj);
    }

    public Page<Endereco> recuperarTodos(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return repository.findAll(pageable);
    }

    public Optional<Endereco> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Endereco atualizar(Endereco obj) {
        return repository.save(obj);
    }

    @Transactional
    public void remover(Endereco obj) {
        repository.delete(obj);
    }
}
