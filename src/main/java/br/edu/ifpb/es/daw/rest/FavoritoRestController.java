package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.mapper.ProdutoMapper;
import br.edu.ifpb.es.daw.rest.dto.response.ProdutoResponseDTO;
import br.edu.ifpb.es.daw.service.FavoritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favoritos")
@PreAuthorize("hasAuthority('CLIENTE')")
@Tag(name = "Favoritos", description = "Lista de desejos do cliente logado")
public class FavoritoRestController {

    @Autowired private FavoritoService service;
    @Autowired private ProdutoMapper produtoMapper;

    @GetMapping
    @Operation(summary = "Produtos favoritados, mais recentes primeiro (12 por página)")
    public ResponseEntity<Page<ProdutoResponseDTO>> listar(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.meus(page).map(produtoMapper::from));
    }

    @GetMapping("/ids")
    @Operation(summary = "Só os ids dos produtos favoritados, para marcar o coração nos cards")
    public ResponseEntity<List<Long>> ids() {
        return ResponseEntity.ok(service.idsDosMeus());
    }

    @PutMapping("/{idProduto}")
    @Operation(summary = "Favoritar um produto (idempotente)")
    public ResponseEntity<Void> adicionar(@PathVariable Long idProduto) {
        service.adicionar(idProduto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idProduto}")
    @Operation(summary = "Desfavoritar um produto (idempotente)")
    public ResponseEntity<Void> remover(@PathVariable Long idProduto) {
        service.remover(idProduto);
        return ResponseEntity.noContent().build();
    }
}
