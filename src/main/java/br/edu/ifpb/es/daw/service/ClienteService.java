package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Cliente criar(Cliente obj) {
        obj.setSenha(passwordEncoder.encode(obj.getSenha()));
        obj.setRole(Role.CLIENTE);
        return repository.save(obj);
    }

    public Page<Cliente> recuperarTodos(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return repository.findAll(pageable);
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
