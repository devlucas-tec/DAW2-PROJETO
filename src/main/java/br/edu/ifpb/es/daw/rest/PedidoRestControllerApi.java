package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.PedidoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PedidoResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "pedidos", description = "API Pedidos")
public interface PedidoRestControllerApi {
    ResponseEntity<Page<PedidoResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);
    ResponseEntity<PedidoResponseDTO> adicionar(@RequestBody PedidoRequestDTO dto);
    ResponseEntity<PedidoResponseDTO> recuperarPor(@PathVariable Long id);
    ResponseEntity<PedidoResponseDTO> fechar(@PathVariable Long id);
    ResponseEntity<PedidoResponseDTO> cancelar(@PathVariable Long id);
    ResponseEntity<Void> remover(@PathVariable Long id);
}
