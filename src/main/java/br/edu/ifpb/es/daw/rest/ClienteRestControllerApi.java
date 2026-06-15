package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.ClienteRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ClienteResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "cliente", description = "API Cliente")
public interface ClienteRestControllerApi {
    ResponseEntity<Page<ClienteResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);
    ResponseEntity<ClienteResponseDTO> adicionar(@RequestBody ClienteRequestDTO dto);
    ResponseEntity<ClienteResponseDTO> recuperarPor(@PathVariable Long id);
    ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id, @RequestBody ClienteRequestDTO dto);
    ResponseEntity<Void> remover(@PathVariable Long id);
}
