package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Admin;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.repository.AdminRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    private static final int PAGE_SIZE = 10;

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

    public Page<Admin> recuperarTodos(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return repository.findAll(pageable);
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
