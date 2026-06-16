package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.EnderecoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.EnderecoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Endereços", description = "API Endereços")
public interface EnderecoRestControllerApi {
    @Operation(summary = "Listar todos os endereços")
    ResponseEntity<Page<EnderecoResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);

    @Operation(summary = "Criar endereço")
    ResponseEntity<EnderecoResponseDTO> adicionar(@RequestBody EnderecoRequestDTO dto);

    @Operation(summary = "Buscar endereço por ID")
    ResponseEntity<EnderecoResponseDTO> recuperarPor(@PathVariable Long id);

    @Operation(summary = "Atualizar endereço")
    ResponseEntity<EnderecoResponseDTO> atualizar(@PathVariable Long id, @RequestBody EnderecoRequestDTO dto);

    @Operation(summary = "Remover endereço")
    ResponseEntity<Void> remover(@PathVariable Long id);
}