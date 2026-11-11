package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.model.Usuario;
import br.edu.ifpb.es.daw.repository.UsuarioRepository;
import br.edu.ifpb.es.daw.rest.dto.request.AuthRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.AuthResponseDTO;
import br.edu.ifpb.es.daw.rest.dto.response.UsuarioLogadoResponseDTO;
import br.edu.ifpb.es.daw.service.JwtService;
import br.edu.ifpb.es.daw.service.UsuarioAutenticadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtService jwtService;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private UsuarioAutenticadoService usuarioAutenticado;

    @PostMapping("/login")
    @Operation(summary = "Realizar login e obter token JWT")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha())
        );

        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", "email", dto.email()));

        String token = jwtService.generateToken(usuario.getEmail(), usuario.getRole());
        return ResponseEntity.ok(new AuthResponseDTO(
                token, usuario.getRole().name(), usuario.getEmail(), usuario.getId(), usuario.getNome()));
    }

    @GetMapping("/me")
    @Operation(summary = "Dados do usuário logado")
    public ResponseEntity<UsuarioLogadoResponseDTO> me() {
        Usuario usuario = usuarioAutenticado.getUsuario();
        return ResponseEntity.ok(new UsuarioLogadoResponseDTO(
                usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole().name()));
    }
}
