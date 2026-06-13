package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.ClienteRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ClienteResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import java.util.List;

@Tag(name = "clientes", description = "API Clientes")
public interface ClienteRestControllerApi {
    @Operation(summary = "Listar todos os clientes") ResponseEntity<List<ClienteResponseDTO>> listar();
    @Operation(summary = "Criar um novo cliente") ResponseEntity<ClienteResponseDTO> adicionar(ClienteRequestDTO dto);
    @Operation(summary = "Buscar cliente por ID") ResponseEntity<ClienteResponseDTO> recuperarPor(Long id);
    @Operation(summary = "Atualizar cliente") ResponseEntity<ClienteResponseDTO> atualizar(Long id, ClienteRequestDTO dto);
    @Operation(summary = "Remover cliente") ResponseEntity<Void> remover(Long id);
}