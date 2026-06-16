package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.AdminRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.AdminResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Admin", description = "API Admin")
public interface AdminRestControllerApi {
    @Operation(summary = "Listar todos os admins")
    ResponseEntity<Page<AdminResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);

    @Operation(summary = "Criar admin")
    ResponseEntity<AdminResponseDTO> adicionar(@RequestBody AdminRequestDTO dto);

    @Operation(summary = "Buscar admin por ID")
    ResponseEntity<AdminResponseDTO> recuperarPor(@PathVariable Long id);

    @Operation(summary = "Atualizar admin")
    ResponseEntity<AdminResponseDTO> atualizar(@PathVariable Long id, @RequestBody AdminRequestDTO dto);

    @Operation(summary = "Remover admin")
    ResponseEntity<Void> remover(@PathVariable Long id);
}