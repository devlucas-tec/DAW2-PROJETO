package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.mapper.AdminMapper;
import br.edu.ifpb.es.daw.rest.dto.request.AdminRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.request.AdminUpdateDTO;
import br.edu.ifpb.es.daw.rest.dto.response.AdminResponseDTO;
import br.edu.ifpb.es.daw.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminRestController implements AdminRestControllerApi {

    @Autowired private AdminMapper mapper;
    @Autowired private AdminService service;

    @Override @GetMapping
    public ResponseEntity<Page<AdminResponseDTO>> listar(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarTodos(page).map(mapper::from));
    }

    @Override @PostMapping
    public ResponseEntity<AdminResponseDTO> adicionar(@RequestBody @Valid AdminRequestDTO dto) {
        return new ResponseEntity<>(mapper.from(service.criar(mapper.from(dto), dto.getSenha())), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<AdminResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.buscarPorId(id)));
    }

    @Override @PatchMapping("/{id}")
    public ResponseEntity<AdminResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AdminUpdateDTO dto) {
        return ResponseEntity.ok(mapper.from(service.atualizar(id, dto)));
    }

    @Override @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
