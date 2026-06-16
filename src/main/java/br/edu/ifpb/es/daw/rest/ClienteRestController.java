package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.mapper.ClienteMapper;
import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.rest.dto.request.ClienteRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ClienteResponseDTO;
import br.edu.ifpb.es.daw.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteRestController implements ClienteRestControllerApi {

    @Autowired private ClienteMapper mapper;
    @Autowired private ClienteService service;

    @Override @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarTodos(page).map(mapper::from));
    }

    @Override @GetMapping("/buscar")
    public ResponseEntity<Page<ClienteResponseDTO>> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.filtrar(nome, email, page).map(mapper::from));
    }

    @Override @PostMapping
    public ResponseEntity<ClienteResponseDTO> adicionar(@RequestBody @Valid ClienteRequestDTO dto) {
        Cliente obj = service.criar(mapper.from(dto));
        return new ResponseEntity<>(mapper.from(obj), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(validarExiste(id)));
    }

    @Override @PatchMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id,
                                                        @RequestBody @Valid ClienteRequestDTO dto) {
        Cliente obj = validarExiste(id);
        obj.setNome(dto.getNome());
        obj.setEmail(dto.getEmail());
        obj.setTelefone(dto.getTelefone());
        return ResponseEntity.ok(mapper.from(service.atualizar(obj)));
    }

    @Override @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(validarExiste(id));
        return ResponseEntity.noContent().build();
    }

    private Cliente validarExiste(Long id) {
        return service.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente", id));
    }
}