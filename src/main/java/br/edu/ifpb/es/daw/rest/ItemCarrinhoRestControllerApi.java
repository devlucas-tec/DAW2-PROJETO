package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.ItemCarrinhoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ItemCarrinhoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import java.util.List;


@Tag(name = "itens-carrinho", description = "API Itens de Carrinho")
public interface ItemCarrinhoRestControllerApi {
    @Operation(summary = "Listar todos os itens de carrinho") ResponseEntity<List<ItemCarrinhoResponseDTO>> listar();
    @Operation(summary = "Adicionar item ao carrinho") ResponseEntity<ItemCarrinhoResponseDTO> adicionar(ItemCarrinhoRequestDTO dto);
}