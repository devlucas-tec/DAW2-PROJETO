package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.mapper.EntregaMapper;
import br.edu.ifpb.es.daw.model.enums.StatusEntrega;
import br.edu.ifpb.es.daw.rest.dto.request.EntregaRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.EntregaResponseDTO;
import br.edu.ifpb.es.daw.service.EntregaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entregas")
public class EntregaRestController implements EntregaRestControllerApi {

    @Autowired private EntregaMapper mapper;
    @Autowired private EntregaService service;

    @Override @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR')")
    public ResponseEntity<EntregaResponseDTO> adicionar(@RequestBody @Valid EntregaRequestDTO dto) {
        return new ResponseEntity<>(mapper.from(service.criar(
                dto.getIdPedido(), dto.getTransportadora(), dto.getCodigoRastreamento())), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<EntregaResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.buscarPorId(id)));
    }

    @Override @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR')")
    public ResponseEntity<EntregaResponseDTO> atualizarStatus(@PathVariable Long id, @RequestParam StatusEntrega status,
            @RequestParam(required = false) String transportadora,
            @RequestParam(required = false) String codigoRastreamento) {
        return ResponseEntity.ok(mapper.from(service.atualizarStatus(id, status, transportadora, codigoRastreamento)));
    }
}
