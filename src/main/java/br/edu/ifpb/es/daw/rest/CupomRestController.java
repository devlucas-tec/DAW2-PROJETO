package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.mapper.CupomMapper;
import br.edu.ifpb.es.daw.model.enums.StatusCupom;
import br.edu.ifpb.es.daw.rest.dto.request.CupomRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CupomResponseDTO;
import br.edu.ifpb.es.daw.service.CupomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cupons")
@PreAuthorize("hasAuthority('ADMIN')")
public class CupomRestController implements CupomRestControllerApi {

    @Autowired private CupomMapper mapper;
    @Autowired private CupomService service;

    @Override @GetMapping
    public ResponseEntity<Page<CupomResponseDTO>> listar(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarTodos(page).map(mapper::from));
    }

    @Override @GetMapping("/buscar")
    public ResponseEntity<Page<CupomResponseDTO>> buscar(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) StatusCupom status,
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.filtrar(codigo, status, page).map(mapper::from));
    }

    @Override @PostMapping
    public ResponseEntity<CupomResponseDTO> adicionar(@RequestBody @Valid CupomRequestDTO dto) {
        return new ResponseEntity<>(mapper.from(service.criar(mapper.from(dto), dto.getStatus())), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<CupomResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.buscarPorId(id)));
    }

    @Override @PatchMapping("/{id}")
    public ResponseEntity<CupomResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid CupomRequestDTO dto) {
        return ResponseEntity.ok(mapper.from(service.atualizar(id, mapper.from(dto), dto.getStatus())));
    }

    @Override @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
