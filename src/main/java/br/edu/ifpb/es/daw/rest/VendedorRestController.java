package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.mapper.VendedorMapper;
import br.edu.ifpb.es.daw.rest.dto.request.VendedorRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.request.VendedorUpdateDTO;
import br.edu.ifpb.es.daw.rest.dto.response.VendedorResponseDTO;
import br.edu.ifpb.es.daw.service.VendedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vendedores")
public class VendedorRestController implements VendedorRestControllerApi {

    @Autowired private VendedorMapper mapper;
    @Autowired private VendedorService service;

    @Override @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<Page<VendedorResponseDTO>> listar(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarTodos(page).map(mapper::from));
    }

    @Override @PostMapping
    public ResponseEntity<VendedorResponseDTO> adicionar(@RequestBody @Valid VendedorRequestDTO dto) {
        return new ResponseEntity<>(mapper.from(service.criar(mapper.from(dto))), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<VendedorResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.buscarPorId(id)));
    }

    @Override @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR')")
    public ResponseEntity<VendedorResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid VendedorUpdateDTO dto) {
        return ResponseEntity.ok(mapper.from(service.atualizar(id, dto)));
    }

    @Override @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
