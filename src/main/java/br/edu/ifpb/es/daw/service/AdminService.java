package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Admin;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.repository.AdminRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Admin criar(Admin obj) {

        obj.setSenha(passwordEncoder.encode(obj.getSenha()));
        obj.setRole(Role.ADMIN);

        return repository.save(obj);
    }

    public List<Admin> recuperarTodos() {

        return repository.findAll();
    }

    public Optional<Admin> buscarPorId(Long id) {

        return repository.findById(id);
    }

}