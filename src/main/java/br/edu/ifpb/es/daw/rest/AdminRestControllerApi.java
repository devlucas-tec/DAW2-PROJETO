package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.AdminRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.AdminResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import java.util.List;

@Tag(name = "admin", description = "API Administradores")
public interface AdminRestControllerApi {
    @Operation(summary = "Listar todos os administradores") ResponseEntity<List<AdminResponseDTO>> listar();
    @Operation(summary = "Criar administrador") ResponseEntity<AdminResponseDTO> adicionar(AdminRequestDTO dto);
    @Operation(summary = "Buscar administrador por ID") ResponseEntity<AdminResponseDTO> recuperarPor(Long id);
}