package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.CategoriaRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CategoriaResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "categoria", description = "API Categoria")
public interface CategoriaRestControllerApi {
    ResponseEntity<Page<CategoriaResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);
    ResponseEntity<CategoriaResponseDTO> adicionar(@RequestBody CategoriaRequestDTO dto);
    ResponseEntity<CategoriaResponseDTO> recuperarPor(@PathVariable Long id);
    ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long id, @RequestBody CategoriaRequestDTO dto);
    ResponseEntity<Void> remover(@PathVariable Long id);
}
