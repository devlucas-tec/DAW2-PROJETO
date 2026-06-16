package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.DevolucaoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.DevolucaoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Devolução", description = "API Devolução")
public interface DevolucaoRestControllerApi {
    @Operation(summary = "Registrar devolução")
    ResponseEntity<DevolucaoResponseDTO> adicionar(DevolucaoRequestDTO dto);

    @Operation(summary = "Buscar devolução por ID")
    ResponseEntity<DevolucaoResponseDTO> recuperarPor(Long id);

    @Operation(summary = "Aprovar devolução")
    ResponseEntity<DevolucaoResponseDTO> aprovar(Long id);

    @Operation(summary = "Rejeitar devolução")
    ResponseEntity<DevolucaoResponseDTO> rejeitar(Long id);
}