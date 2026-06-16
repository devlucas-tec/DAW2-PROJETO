package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.CategoriaRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CategoriaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Categorias", description = "API Categorias")
public interface CategoriaRestControllerApi {
    @Operation(summary = "Listar todas as categorias")
    ResponseEntity<Page<CategoriaResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);

    @Operation(summary = "Criar categoria")
    ResponseEntity<CategoriaResponseDTO> adicionar(@RequestBody CategoriaRequestDTO dto);

    @Operation(summary = "Buscar categoria por ID")
    ResponseEntity<CategoriaResponseDTO> recuperarPor(@PathVariable Long id);

    @Operation(summary = "Atualizar categoria")
    ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long id, @RequestBody CategoriaRequestDTO dto);

    @Operation(summary = "Remover categoria")
    ResponseEntity<Void> remover(@PathVariable Long id);
}