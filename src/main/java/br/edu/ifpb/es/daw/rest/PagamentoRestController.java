package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.mapper.PagamentoMapper;
import br.edu.ifpb.es.daw.rest.dto.request.PagamentoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PagamentoResponseDTO;
import br.edu.ifpb.es.daw.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoRestController implements PagamentoRestControllerApi {

    @Autowired private PagamentoMapper mapper;
    @Autowired private PagamentoService service;

    @Override @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENTE')")
    public ResponseEntity<PagamentoResponseDTO> adicionar(@RequestBody @Valid PagamentoRequestDTO dto) {
        return new ResponseEntity<>(mapper.from(service.criar(dto.getIdPedido(), dto.getMetodo())), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<PagamentoResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.buscarPorId(id)));
    }

    @Override @PatchMapping("/{id}/confirmar")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR')")
    public ResponseEntity<PagamentoResponseDTO> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.confirmar(id)));
    }

    @Override @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENTE')")
    public ResponseEntity<PagamentoResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.cancelar(id)));
    }
}
