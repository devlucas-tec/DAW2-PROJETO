package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Vendedor;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.repository.VendedorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendedorService {

    @Autowired
    private VendedorRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Vendedor criar(Vendedor obj) {

        obj.setSenha(passwordEncoder.encode(obj.getSenha()));
        obj.setRole(Role.VENDEDOR);

        return repository.save(obj);
    }

    public List<Vendedor> recuperarTodos() {

        return repository.findAll();
    }

    public Optional<Vendedor> buscarPorId(Long id) {

        return repository.findById(id);
    }

    @Transactional
    public Vendedor atualizar(Vendedor obj) {

        return repository.save(obj);
    }

    @Transactional
    public void remover(Vendedor obj) {

        repository.delete(obj);
    }
}