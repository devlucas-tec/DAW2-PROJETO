package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.ItemCarrinhoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ItemCarrinhoResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "item-carrinho", description = "API Itens carrinho")
public interface ItemCarrinhoRestControllerApi {
    ResponseEntity<Page<ItemCarrinhoResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);
    ResponseEntity<ItemCarrinhoResponseDTO> adicionar(@RequestBody ItemCarrinhoRequestDTO dto);
}
