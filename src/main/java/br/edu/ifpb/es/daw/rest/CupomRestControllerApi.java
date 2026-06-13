package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.CupomRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CupomResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import java.util.List;

@Tag(name = "cupons", description = "API Cupons")
public interface CupomRestControllerApi {
    @Operation(summary = "Listar todos os cupons") ResponseEntity<List<CupomResponseDTO>> listar();
    @Operation(summary = "Criar cupom") ResponseEntity<CupomResponseDTO> adicionar(CupomRequestDTO dto);
    @Operation(summary = "Buscar cupom por ID") ResponseEntity<CupomResponseDTO> recuperarPor(Long id);
    @Operation(summary = "Atualizar cupom") ResponseEntity<CupomResponseDTO> atualizar(Long id, CupomRequestDTO dto);
    @Operation(summary = "Remover cupom") ResponseEntity<Void> remover(Long id);
}