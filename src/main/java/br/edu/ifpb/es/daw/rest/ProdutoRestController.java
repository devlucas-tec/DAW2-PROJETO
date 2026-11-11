package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.mapper.ProdutoMapper;
import br.edu.ifpb.es.daw.model.Categoria;
import br.edu.ifpb.es.daw.model.Usuario;
import br.edu.ifpb.es.daw.model.Vendedor;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.rest.dto.request.ProdutoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.request.ProdutoUpdateDTO;
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
    public ResponseEntity<Page<ProdutoResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(service.buscarVitrine(null, null, null, null, null, true, null, page, size)
                .map(mapper::from));
    }

    @Override @GetMapping("/buscar")
    public ResponseEntity<Page<ProdutoResponseDTO>> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) Long idVendedor,
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax,
            @RequestParam(required = false) Boolean somenteComEstoque,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(service.buscarVitrine(nome, idCategoria, idVendedor, precoMin, precoMax,
                somenteComEstoque, sort, page, size).map(mapper::from));
    }

    @Override @GetMapping("/meus")
    @PreAuthorize("hasAuthority('VENDEDOR')")
    public ResponseEntity<Page<ProdutoResponseDTO>> meus(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(service.buscarMeus(nome, ativo, page, size).map(mapper::from));
    }

    @Override @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR')")
    public ResponseEntity<ProdutoResponseDTO> adicionar(@RequestBody @Valid ProdutoRequestDTO dto) {
        Usuario usuario = usuarioAutenticado.getUsuario();
        Long idVendedor;
        if (usuario.getRole() == Role.ADMIN) {
            if (dto.getIdVendedor() == null) {
                throw new IllegalArgumentException("O campo 'idVendedor' é obrigatório para ADMIN.");
            }
            idVendedor = dto.getIdVendedor();
        } else {
            // O VENDEDOR sempre cadastra em nome próprio; o idVendedor do corpo é ignorado.
            idVendedor = usuario.getId();
        }
        Vendedor vendedor = vendedorService.buscarPorId(idVendedor);
        Categoria categoria = categoriaService.buscarPorId(dto.getIdCategoria());
        return new ResponseEntity<>(mapper.from(service.criar(mapper.from(dto), categoria, vendedor)), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.buscarVisivel(id)));
    }

    @Override @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR')")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoUpdateDTO dto) {
        Categoria categoria = dto.getIdCategoria() != null ? categoriaService.buscarPorId(dto.getIdCategoria()) : null;
        return ResponseEntity.ok(mapper.from(service.atualizar(id, dto, categoria)));
    }

    @Override @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR')")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
