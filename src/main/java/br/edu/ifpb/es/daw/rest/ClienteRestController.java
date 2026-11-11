package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.mapper.ClienteMapper;
import br.edu.ifpb.es.daw.rest.dto.request.ClienteRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.request.ClienteUpdateDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ClienteResponseDTO;
import br.edu.ifpb.es.daw.service.ClienteService;
import br.edu.ifpb.es.daw.rest.dto.request.AlterarSenhaDTO;
import br.edu.ifpb.es.daw.service.UsuarioAutenticadoService;
import br.edu.ifpb.es.daw.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteRestController implements ClienteRestControllerApi {

    @Autowired private ClienteMapper mapper;
    @Autowired private ClienteService service;
    @Autowired private UsuarioService usuarioService;
    @Autowired private UsuarioAutenticadoService usuarioAutenticado;

    @Override @PatchMapping("/me/senha")
    @PreAuthorize("hasAuthority('CLIENTE')")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid AlterarSenhaDTO dto) {
        usuarioService.alterarSenha(usuarioAutenticado.getUsuario(), dto.getSenhaAtual(), dto.getNovaSenha());
        return ResponseEntity.noContent().build();
    }

    @Override @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<ClienteResponseDTO>> listar(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarTodos(page).map(mapper::from));
    }

    @Override @GetMapping("/buscar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<ClienteResponseDTO>> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.filtrar(nome, email, page).map(mapper::from));
    }

    @Override @PostMapping
    public ResponseEntity<ClienteResponseDTO> adicionar(@RequestBody @Valid ClienteRequestDTO dto) {
        return new ResponseEntity<>(mapper.from(service.criar(mapper.from(dto))), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENTE')")
    public ResponseEntity<ClienteResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.buscarComAcesso(id)));
    }

    @Override @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENTE')")
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ClienteUpdateDTO dto) {
        return ResponseEntity.ok(mapper.from(service.atualizar(id, dto)));
    }

    @Override @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
