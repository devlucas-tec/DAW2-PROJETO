package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Endereco;
import br.edu.ifpb.es.daw.repository.EnderecoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository repository;

    @Transactional
    public Endereco criar(Endereco obj) {

        return repository.save(obj);
    }

    public List<Endereco> recuperarTodos() {

        return repository.findAll();
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
