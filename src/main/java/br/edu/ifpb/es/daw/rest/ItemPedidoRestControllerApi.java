package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.ItemPedidoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ItemPedidoResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "item-pedido", description = "API Itens Pedido")
public interface ItemPedidoRestControllerApi {
    ResponseEntity<Page<ItemPedidoResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);
    ResponseEntity<ItemPedidoResponseDTO> adicionar(@RequestBody ItemPedidoRequestDTO dto);
}
