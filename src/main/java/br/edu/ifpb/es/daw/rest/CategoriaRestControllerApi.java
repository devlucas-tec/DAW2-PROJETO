package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.CategoriaRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CategoriaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import java.util.List;

@Tag(name = "categorias", description = "API Categorias")
public interface CategoriaRestControllerApi {
    @Operation(summary = "Listar todas as categorias") ResponseEntity<List<CategoriaResponseDTO>> listar();
    @Operation(summary = "Criar categoria") ResponseEntity<CategoriaResponseDTO> adicionar(CategoriaRequestDTO dto);
    @Operation(summary = "Buscar categoria por ID") ResponseEntity<CategoriaResponseDTO> recuperarPor(Long id);
    @Operation(summary = "Atualizar categoria") ResponseEntity<CategoriaResponseDTO> atualizar(Long id, CategoriaRequestDTO dto);
    @Operation(summary = "Remover categoria") ResponseEntity<Void> remover(Long id);
}