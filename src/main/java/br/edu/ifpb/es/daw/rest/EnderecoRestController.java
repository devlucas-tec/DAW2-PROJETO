package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.mapper.EnderecoMapper;
import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Endereco;
import br.edu.ifpb.es.daw.rest.dto.request.EnderecoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.EnderecoResponseDTO;
import br.edu.ifpb.es.daw.service.ClienteService;
import br.edu.ifpb.es.daw.service.EnderecoService;
import br.edu.ifpb.es.daw.service.UsuarioAutenticadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enderecos")
@PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENTE')")
public class EnderecoRestController implements EnderecoRestControllerApi {

    @Autowired private EnderecoMapper mapper;
    @Autowired private EnderecoService service;
    @Autowired private ClienteService clienteService;
    @Autowired private UsuarioAutenticadoService usuarioAutenticado;

    @Override @GetMapping
    public ResponseEntity<Page<EnderecoResponseDTO>> listar(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarTodos(page).map(mapper::from));
    }

    @Override @PostMapping
    public ResponseEntity<EnderecoResponseDTO> adicionar(@RequestBody @Valid EnderecoRequestDTO dto) {
        Long idCliente = usuarioAutenticado.resolverIdUsuario(dto.getIdCliente(), "idCliente");
        Cliente cliente = clienteService.buscarPorId(idCliente);
        return new ResponseEntity<>(mapper.from(service.criar(mapper.from(dto), cliente)), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.buscarPorId(id)));
    }

    @Override @PatchMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid EnderecoRequestDTO dto) {
        Endereco endereco = service.buscarPorId(id);
        mapper.atualizar(dto, endereco);
        return ResponseEntity.ok(mapper.from(service.atualizar(endereco)));
    }

    @Override @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
