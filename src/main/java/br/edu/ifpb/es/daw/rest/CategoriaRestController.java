package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.mapper.CategoriaMapper;
import br.edu.ifpb.es.daw.rest.dto.request.CategoriaRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CategoriaResponseDTO;
import br.edu.ifpb.es.daw.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categorias")
public class CategoriaRestController implements CategoriaRestControllerApi {

    @Autowired private CategoriaMapper mapper;
    @Autowired private CategoriaService service;

    @Override @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<Page<CategoriaResponseDTO>> listar(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarTodos(page).map(mapper::from));
    }

    @Override @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR')")
    public ResponseEntity<CategoriaResponseDTO> adicionar(@RequestBody @Valid CategoriaRequestDTO dto) {
        return new ResponseEntity<>(mapper.from(service.criar(mapper.from(dto))), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<CategoriaResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.buscarPorId(id)));
    }

    @Override @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR')")
    public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid CategoriaRequestDTO dto) {
        return ResponseEntity.ok(mapper.from(service.atualizar(id, mapper.from(dto))));
    }

    @Override @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
