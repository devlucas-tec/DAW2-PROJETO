package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.EnderecoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.EnderecoResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "endereços", description = "API Endereços")
public interface EnderecoRestControllerApi {
    ResponseEntity<Page<EnderecoResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);
    ResponseEntity<EnderecoResponseDTO> adicionar(@RequestBody EnderecoRequestDTO dto);
    ResponseEntity<EnderecoResponseDTO> recuperarPor(@PathVariable Long id);
    ResponseEntity<EnderecoResponseDTO> atualizar(@PathVariable Long id, @RequestBody EnderecoRequestDTO dto);
    ResponseEntity<Void> remover(@PathVariable Long id);
}
