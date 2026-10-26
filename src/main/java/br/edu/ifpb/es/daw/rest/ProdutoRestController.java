package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.mapper.ProdutoMapper;
import br.edu.ifpb.es.daw.model.Categoria;
import br.edu.ifpb.es.daw.model.Vendedor;
import br.edu.ifpb.es.daw.rest.dto.request.ProdutoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ProdutoResponseDTO;
import br.edu.ifpb.es.daw.service.CategoriaService;
import br.edu.ifpb.es.daw.service.ProdutoService;
import br.edu.ifpb.es.daw.service.UsuarioAutenticadoService;
import br.edu.ifpb.es.daw.service.VendedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/produtos")
public class ProdutoRestController implements ProdutoRestControllerApi {

    @Autowired private ProdutoMapper mapper;
    @Autowired private ProdutoService service;
    @Autowired private CategoriaService categoriaService;
    @Autowired private VendedorService vendedorService;
    @Autowired private UsuarioAutenticadoService usuarioAutenticado;

    @Override @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<Page<ProdutoResponseDTO>> listar(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarTodos(page).map(mapper::from));
    }

    @Override @GetMapping("/buscar")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<Page<ProdutoResponseDTO>> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax,
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.filtrar(nome, idCategoria, precoMin, precoMax, page).map(mapper::from));
    }

    @Override @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR')")
    public ResponseEntity<ProdutoResponseDTO> adicionar(@RequestBody @Valid ProdutoRequestDTO dto) {
        Long idVendedor = usuarioAutenticado.resolverIdUsuario(dto.getIdVendedor(), "idVendedor");
        Vendedor vendedor = vendedorService.buscarPorId(idVendedor);
        Categoria categoria = categoriaService.buscarPorId(dto.getIdCategoria());
        return new ResponseEntity<>(mapper.from(service.criar(mapper.from(dto), categoria, vendedor)), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<ProdutoResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.buscarPorId(id)));
    }

    @Override @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR')")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoRequestDTO dto) {
        Categoria categoria = categoriaService.buscarPorId(dto.getIdCategoria());
        return ResponseEntity.ok(mapper.from(service.atualizar(id, mapper.from(dto), categoria)));
    }

    @Override @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR')")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
