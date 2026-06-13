package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.PedidoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PedidoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import java.util.List;

@Tag(name = "pedidos", description = "API Pedidos")
public interface PedidoRestControllerApi {
    @Operation(summary = "Listar todos os pedidos") ResponseEntity<List<PedidoResponseDTO>> listar();
    @Operation(summary = "Criar pedido") ResponseEntity<PedidoResponseDTO> adicionar(PedidoRequestDTO dto);
    @Operation(summary = "Buscar pedido por ID") ResponseEntity<PedidoResponseDTO> recuperarPor(Long id);
    @Operation(summary = "Fechar pedido") ResponseEntity<PedidoResponseDTO> fechar(Long id);
    @Operation(summary = "Cancelar pedido") ResponseEntity<PedidoResponseDTO> cancelar(Long id);
    @Operation(summary = "Remover pedido") ResponseEntity<Void> remover(Long id);
}