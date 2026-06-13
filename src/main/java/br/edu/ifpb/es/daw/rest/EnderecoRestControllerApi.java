package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.EnderecoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.EnderecoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import java.util.List;

@Tag(name = "enderecos", description = "API Endereços")
public interface EnderecoRestControllerApi {
    @Operation(summary = "Listar todos os endereços") ResponseEntity<List<EnderecoResponseDTO>> listar();
    @Operation(summary = "Criar endereço") ResponseEntity<EnderecoResponseDTO> adicionar(EnderecoRequestDTO dto);
    @Operation(summary = "Buscar endereço por ID") ResponseEntity<EnderecoResponseDTO> recuperarPor(Long id);
    @Operation(summary = "Atualizar endereço") ResponseEntity<EnderecoResponseDTO> atualizar(Long id, EnderecoRequestDTO dto);
    @Operation(summary = "Remover endereço") ResponseEntity<Void> remover(Long id);
}