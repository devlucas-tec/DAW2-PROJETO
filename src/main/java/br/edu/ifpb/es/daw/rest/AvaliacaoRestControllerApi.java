package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.AvaliacaoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.AvaliacaoResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "avaliações", description = "API Avaliações")
public interface AvaliacaoRestControllerApi {
    ResponseEntity<Page<AvaliacaoResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);
    ResponseEntity<AvaliacaoResponseDTO> adicionar(@RequestBody AvaliacaoRequestDTO dto);
    ResponseEntity<AvaliacaoResponseDTO> recuperarPor(@PathVariable Long id);
    ResponseEntity<Void> remover(@PathVariable Long id);
}
