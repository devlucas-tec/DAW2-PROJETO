package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.ConflitoException;
import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.exception.EstadoInvalidoException;
import br.edu.ifpb.es.daw.model.Usuario;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticado;

    public void validarEmailDisponivel(String email, Long idUsuarioAtual) {
        usuarioRepository.findByEmail(email)
                .filter(existente -> !Objects.equals(existente.getId(), idUsuarioAtual))
                .ifPresent(existente -> {
                    throw new ConflitoException("Já existe um usuário cadastrado com o e-mail '" + email + "'.");
                });
    }

    public void atualizarDadosBasicos(Usuario usuario, String nome, String email, String senha) {
        if (nome != null) {
            usuario.setNome(nome);
        }
        if (email != null && !email.equalsIgnoreCase(usuario.getEmail())) {
            validarEmailDisponivel(email, usuario.getId());
            usuario.setEmail(email);
        }
        if (senha != null) {
            usuario.setSenha(passwordEncoder.encode(senha));
        }
    }

    public String codificarSenha(String senha) {
        return passwordEncoder.encode(senha);
    }

    /** Troca a senha do usuário logado, conferindo a senha atual. */
    @Transactional
    public void alterarSenha(Usuario usuario, String senhaAtual, String novaSenha) {
        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new IllegalArgumentException("A senha atual está incorreta.");
        }
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
    }

    public Page<Usuario> filtrar(String nome, Role role,
                                                                 Boolean ativo, int page) {
        return usuarioRepository.filtrar(nome != null && nome.isBlank() ? null : nome, role, ativo,
                PageRequest.of(Math.max(page, 0), 10,
                        Sort.by("nome").and(Sort.by("id"))));
    }

    /** Bloqueia ou desbloqueia um usuário. O ADMIN não pode bloquear a si mesmo. */
    @Transactional
    public Usuario alterarAtivo(Long id, boolean ativo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", id));
        if (!ativo && Objects.equals(usuario.getId(), usuarioAutenticado.getUsuario().getId())) {
            throw new EstadoInvalidoException("Você não pode bloquear a si mesmo.");
        }
        usuario.setAtivo(ativo);
        return usuarioRepository.save(usuario);
    }
}
