package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.PedidoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PedidoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Pedidos", description = "API Pedidos")
public interface PedidoRestControllerApi {
    @Operation(summary = "Listar todos os pedidos")
    ResponseEntity<Page<PedidoResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);

    @Operation(summary = "Criar pedido")
    ResponseEntity<PedidoResponseDTO> adicionar(@RequestBody PedidoRequestDTO dto);

    @Operation(summary = "Buscar pedido por ID")
    ResponseEntity<PedidoResponseDTO> recuperarPor(@PathVariable Long id);

    @Operation(summary = "Fechar pedido")
    ResponseEntity<PedidoResponseDTO> fechar(@PathVariable Long id);

    @Operation(summary = "Cancelar pedido")
    ResponseEntity<PedidoResponseDTO> cancelar(@PathVariable Long id);

    @Operation(summary = "Remover pedido")
    ResponseEntity<Void> remover(@PathVariable Long id);
}