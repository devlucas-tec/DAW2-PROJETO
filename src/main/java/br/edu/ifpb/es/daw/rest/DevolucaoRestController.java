package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.mapper.DevolucaoMapper;
import br.edu.ifpb.es.daw.rest.dto.request.DevolucaoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.DevolucaoResponseDTO;
import br.edu.ifpb.es.daw.service.DevolucaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devolucoes")
public class DevolucaoRestController implements DevolucaoRestControllerApi {

    @Autowired private DevolucaoMapper mapper;
    @Autowired private DevolucaoService service;

    @Override @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENTE')")
    public ResponseEntity<DevolucaoResponseDTO> adicionar(@RequestBody @Valid DevolucaoRequestDTO dto) {
        return new ResponseEntity<>(mapper.from(service.criar(dto.getIdPedido(), dto.getMotivo())), HttpStatus.CREATED);
    }

    @Override @GetMapping("/minhas")
    @PreAuthorize("hasAuthority('CLIENTE')")
    public ResponseEntity<Page<DevolucaoResponseDTO>> minhas(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarMinhas(page).map(mapper::from));
    }

    @Override @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<DevolucaoResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.buscarPorId(id)));
    }

    @Override @PatchMapping("/{id}/aprovar")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR')")
    public ResponseEntity<DevolucaoResponseDTO> aprovar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.aprovar(id)));
    }

    @Override @PatchMapping("/{id}/rejeitar")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR')")
    public ResponseEntity<DevolucaoResponseDTO> rejeitar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.rejeitar(id)));
    }
}
