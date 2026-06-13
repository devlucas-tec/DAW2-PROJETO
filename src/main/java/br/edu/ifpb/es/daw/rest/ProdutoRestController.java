package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.mapper.ProdutoMapper;
import br.edu.ifpb.es.daw.model.Categoria;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.model.Vendedor;
import br.edu.ifpb.es.daw.rest.dto.request.ProdutoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ProdutoResponseDTO;
import br.edu.ifpb.es.daw.service.CategoriaService;
import br.edu.ifpb.es.daw.service.ProdutoService;
import br.edu.ifpb.es.daw.service.VendedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoRestController implements ProdutoRestControllerApi {

    @Autowired private ProdutoMapper mapper;
    @Autowired private ProdutoService service;
    @Autowired private CategoriaService categoriaService;
    @Autowired private VendedorService vendedorService;

    @Override @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listar() {
        return ResponseEntity.ok(service.recuperarTodos().stream().map(mapper::from).toList());
    }

    @Override @PostMapping
    public ResponseEntity<ProdutoResponseDTO> adicionar(@RequestBody @Valid ProdutoRequestDTO dto) {
        Produto obj = mapper.from(dto);
        Categoria categoria = categoriaService.buscarPorId(dto.getIdCategoria())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Categoria", dto.getIdCategoria()));
        Vendedor vendedor = vendedorService.buscarPorId(dto.getIdVendedor())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Vendedor", dto.getIdVendedor()));
        obj.setCategoria(categoria);
        obj.setVendedor(vendedor);
        return new ResponseEntity<>(mapper.from(service.criar(obj)), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(validarExiste(id)));
    }

    @Override @PatchMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoRequestDTO dto) {
        Produto obj = validarExiste(id);
        obj.setNome(dto.getNome());
        obj.setDescricao(dto.getDescricao());
        obj.setPreco(dto.getPreco());
        obj.setEstoque(dto.getEstoque());
        if (dto.getIdCategoria() != null) {
            Categoria categoria = categoriaService.buscarPorId(dto.getIdCategoria())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Categoria", dto.getIdCategoria()));
            obj.setCategoria(categoria);
        }
        return ResponseEntity.ok(mapper.from(service.atualizar(obj)));
    }

    @Override @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(validarExiste(id));
        return ResponseEntity.noContent().build();
    }

    private Produto validarExiste(Long id) {
        return service.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto", id));
    }
}