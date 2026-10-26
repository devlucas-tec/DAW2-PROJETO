package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.ItemPedidoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ItemPedidoResponseDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PedidoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Item-pedido", description = "API Itens Pedido")
public interface ItemPedidoRestControllerApi {
    @Operation(summary = "Listar todos os itens de pedido")
    ResponseEntity<Page<ItemPedidoResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);

    @Operation(summary = "Adicionar item ao pedido (se o produto já estiver no pedido, a quantidade é substituída)")
    ResponseEntity<ItemPedidoResponseDTO> adicionar(@RequestBody ItemPedidoRequestDTO dto);

    @Operation(summary = "Remover item do pedido")
    ResponseEntity<PedidoResponseDTO> remover(@PathVariable Long idPedido, @PathVariable Long idProduto);
}
