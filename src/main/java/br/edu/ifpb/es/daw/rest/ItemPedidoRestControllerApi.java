package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.ItemPedidoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ItemPedidoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import java.util.List;

@Tag(name = "itens-pedido", description = "API Itens de Pedido")
public interface ItemPedidoRestControllerApi {
    @Operation(summary = "Listar todos os itens de pedido") ResponseEntity<List<ItemPedidoResponseDTO>> listar();
    @Operation(summary = "Adicionar item ao pedido") ResponseEntity<ItemPedidoResponseDTO> adicionar(ItemPedidoRequestDTO dto);
}