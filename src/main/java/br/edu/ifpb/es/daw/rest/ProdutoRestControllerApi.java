package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.ProdutoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ProdutoResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "produtos", description = "API Produtos")
public interface ProdutoRestControllerApi {
    ResponseEntity<Page<ProdutoResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);
    ResponseEntity<ProdutoResponseDTO> adicionar(@RequestBody ProdutoRequestDTO dto);
    ResponseEntity<ProdutoResponseDTO> recuperarPor(@PathVariable Long id);
    ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @RequestBody ProdutoRequestDTO dto);
    ResponseEntity<Void> remover(@PathVariable Long id);
}
