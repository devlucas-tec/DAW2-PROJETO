package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.model.enums.StatusEntrega;
import br.edu.ifpb.es.daw.rest.dto.request.EntregaRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.EntregaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Entregas", description = "API Entregas")
public interface EntregaRestControllerApi {
    @Operation(summary = "Registrar entrega") ResponseEntity<EntregaResponseDTO> adicionar(EntregaRequestDTO dto);
    @Operation(summary = "Buscar entrega por ID") ResponseEntity<EntregaResponseDTO> recuperarPor(Long id);
    @Operation(summary = "Avançar o status da entrega (só a próxima etapa; EM_TRANSITO exige transportadora e rastreio)") ResponseEntity<EntregaResponseDTO> atualizarStatus(Long id, StatusEntrega status, String transportadora, String codigoRastreamento);
}