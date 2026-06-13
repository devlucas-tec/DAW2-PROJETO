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
    public Admin criar(Admin obj, String senhaTextoPuro) {

        obj.setSenha(passwordEncoder.encode(senhaTextoPuro));
        obj.setRole(Role.ADMIN);

        return repository.save(obj);
    }

    public List<Admin> recuperarTodos() {

        return repository.findAll();
    }

    public Optional<Admin> buscarPorId(Long id) {

        return repository.findById(id);
    }

    @Transactional
    public Admin atualizar(Admin obj, String novaSenhaTextoPuro) {

        if (novaSenhaTextoPuro != null && !novaSenhaTextoPuro.isBlank()) {
            obj.setSenha(passwordEncoder.encode(novaSenhaTextoPuro));
        }

        return repository.save(obj);
    }

    @Transactional
    public void remover(Admin obj) {

        repository.delete(obj);
    }
}