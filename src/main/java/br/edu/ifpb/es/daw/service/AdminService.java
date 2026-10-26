package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.config.StartupListener;
import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.exception.EstadoInvalidoException;
import br.edu.ifpb.es.daw.model.Admin;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.repository.AdminRepository;
import br.edu.ifpb.es.daw.rest.dto.request.AdminUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class AdminService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private AdminRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticado;

    @Transactional
    public Admin criar(Admin obj, String senhaTextoPuro) {
        usuarioService.validarEmailDisponivel(obj.getEmail(), null);
        obj.setSenha(usuarioService.codificarSenha(senhaTextoPuro));
        obj.setRole(Role.ADMIN);
        return repository.save(obj);
    }

    public Page<Admin> recuperarTodos(int page) {
        return repository.findAll(PageRequest.of(page, PAGE_SIZE));
    }

    public Admin buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Admin", id));
    }

    @Transactional
    public Admin atualizar(Long id, AdminUpdateDTO dto) {
        Admin admin = buscarPorId(id);
        if (StartupListener.ADMIN_EMAIL.equalsIgnoreCase(admin.getEmail())
                && dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(admin.getEmail())) {
            throw new EstadoInvalidoException("O e-mail do admin padrão não pode ser alterado.");
        }
        usuarioService.atualizarDadosBasicos(admin, dto.getNome(), dto.getEmail(), dto.getSenha());
        return repository.save(admin);
    }

    @Transactional
    public void remover(Long id) {
        Admin admin = buscarPorId(id);
        if (Objects.equals(admin.getId(), usuarioAutenticado.getUsuario().getId())) {
            throw new EstadoInvalidoException("Um admin não pode remover a própria conta.");
        }
        if (StartupListener.ADMIN_EMAIL.equalsIgnoreCase(admin.getEmail())) {
            throw new EstadoInvalidoException("O admin padrão não pode ser removido.");
        }
        repository.delete(admin);
        repository.flush();
    }
}
