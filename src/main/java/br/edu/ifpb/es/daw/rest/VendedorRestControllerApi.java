package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.VendedorRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.VendedorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import java.util.List;

@Tag(name = "vendedores", description = "API Vendedores")
public interface VendedorRestControllerApi {
    @Operation(summary = "Listar todos os vendedores") ResponseEntity<List<VendedorResponseDTO>> listar();
    @Operation(summary = "Criar vendedor") ResponseEntity<VendedorResponseDTO> adicionar(VendedorRequestDTO dto);
    @Operation(summary = "Buscar vendedor por ID") ResponseEntity<VendedorResponseDTO> recuperarPor(Long id);
    @Operation(summary = "Atualizar vendedor") ResponseEntity<VendedorResponseDTO> atualizar(Long id, VendedorRequestDTO dto);
    @Operation(summary = "Remover vendedor") ResponseEntity<Void> remover(Long id);
}