package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.ConflitoException;
import br.edu.ifpb.es.daw.model.Usuario;
import br.edu.ifpb.es.daw.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
}
