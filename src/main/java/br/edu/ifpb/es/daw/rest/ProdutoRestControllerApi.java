package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.ProdutoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ProdutoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import java.util.List;

@Tag(name = "produtos", description = "API Produtos")
public interface ProdutoRestControllerApi {
    @Operation(summary = "Listar todos os produtos") ResponseEntity<List<ProdutoResponseDTO>> listar();
    @Operation(summary = "Criar produto") ResponseEntity<ProdutoResponseDTO> adicionar(ProdutoRequestDTO dto);
    @Operation(summary = "Buscar produto por ID") ResponseEntity<ProdutoResponseDTO> recuperarPor(Long id);
    @Operation(summary = "Atualizar produto") ResponseEntity<ProdutoResponseDTO> atualizar(Long id, ProdutoRequestDTO dto);
    @Operation(summary = "Remover produto") ResponseEntity<Void> remover(Long id);
}