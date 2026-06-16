package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.ClienteRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ClienteResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Clientes", description = "API Clientes")
public interface ClienteRestControllerApi {
    @Operation(summary = "Listar todos os clientes")
    ResponseEntity<Page<ClienteResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);

    @Operation(summary = "Buscar clientes por nome e/ou email")
    ResponseEntity<Page<ClienteResponseDTO>> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page
    );

    @Operation(summary = "Criar cliente")
    ResponseEntity<ClienteResponseDTO> adicionar(@RequestBody ClienteRequestDTO dto);

    @Operation(summary = "Buscar cliente por ID")
    ResponseEntity<ClienteResponseDTO> recuperarPor(@PathVariable Long id);

    @Operation(summary = "Atualizar cliente")
    ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id, @RequestBody ClienteRequestDTO dto);

    @Operation(summary = "Remover cliente")
    ResponseEntity<Void> remover(@PathVariable Long id);
}