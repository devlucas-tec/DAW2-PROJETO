package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Transactional
    public Cliente criar(Cliente obj) {

        return repository.save(obj);
    }

    public List<Cliente> recuperarTodos() {

        return repository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {

        return repository.findById(id);
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return repository.buscarPorEmail(email);
    }

    @Transactional
    public Cliente atualizar(Cliente obj) {

        return repository.save(obj);
    }

    @Transactional
    public void remover(Cliente obj) {

        repository.delete(obj);
    }
}
