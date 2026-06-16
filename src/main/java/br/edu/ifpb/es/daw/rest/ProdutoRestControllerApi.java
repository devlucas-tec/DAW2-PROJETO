package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.ProdutoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ProdutoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Tag(name = "Produtos", description = "API Produtos")
public interface ProdutoRestControllerApi {
    @Operation(summary = "Listar todos os produtos")
    ResponseEntity<Page<ProdutoResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);

    @Operation(summary = "Buscar produtos por nome, categoria e/ou faixa de preço")
    ResponseEntity<Page<ProdutoResponseDTO>> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax,
            @RequestParam(defaultValue = "0") int page
    );

    @Operation(summary = "Criar produto")
    ResponseEntity<ProdutoResponseDTO> adicionar(@RequestBody ProdutoRequestDTO dto);

    @Operation(summary = "Buscar produto por ID")
    ResponseEntity<ProdutoResponseDTO> recuperarPor(@PathVariable Long id);

    @Operation(summary = "Atualizar produto")
    ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @RequestBody ProdutoRequestDTO dto);

    @Operation(summary = "Remover produto")
    ResponseEntity<Void> remover(@PathVariable Long id);
}