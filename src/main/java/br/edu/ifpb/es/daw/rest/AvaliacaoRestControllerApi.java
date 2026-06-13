package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.AvaliacaoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.AvaliacaoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import java.util.List;

@Tag(name = "avaliacoes", description = "API Avaliações")
public interface AvaliacaoRestControllerApi {
    @Operation(summary = "Listar todas as avaliações") ResponseEntity<List<AvaliacaoResponseDTO>> listar();
    @Operation(summary = "Criar avaliação") ResponseEntity<AvaliacaoResponseDTO> adicionar(AvaliacaoRequestDTO dto);
    @Operation(summary = "Buscar avaliação por ID") ResponseEntity<AvaliacaoResponseDTO> recuperarPor(Long id);
    @Operation(summary = "Remover avaliação") ResponseEntity<Void> remover(Long id);
}