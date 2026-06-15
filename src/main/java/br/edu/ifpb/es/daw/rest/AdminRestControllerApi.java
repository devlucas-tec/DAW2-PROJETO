package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.AdminRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.AdminResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "admin", description = "API Admin")
public interface AdminRestControllerApi {
    ResponseEntity<Page<AdminResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);
    ResponseEntity<AdminResponseDTO> adicionar(@RequestBody AdminRequestDTO dto);
    ResponseEntity<AdminResponseDTO> recuperarPor(@PathVariable Long id);
    ResponseEntity<AdminResponseDTO> atualizar(@PathVariable Long id, @RequestBody AdminRequestDTO dto);
    ResponseEntity<Void> remover(@PathVariable Long id);
}
