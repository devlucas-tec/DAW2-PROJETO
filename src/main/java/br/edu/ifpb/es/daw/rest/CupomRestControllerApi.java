package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.CupomRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CupomResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "cupom", description = "API Cupom")
public interface CupomRestControllerApi {
    ResponseEntity<Page<CupomResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);
    ResponseEntity<CupomResponseDTO> adicionar(@RequestBody CupomRequestDTO dto);
    ResponseEntity<CupomResponseDTO> recuperarPor(@PathVariable Long id);
    ResponseEntity<CupomResponseDTO> atualizar(@PathVariable Long id, @RequestBody CupomRequestDTO dto);
    ResponseEntity<Void> remover(@PathVariable Long id);
}
