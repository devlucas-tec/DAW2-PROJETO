package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.mapper.CarrinhoMapper;
import br.edu.ifpb.es.daw.mapper.ItemCarrinhoMapper;
import br.edu.ifpb.es.daw.model.Carrinho;
import br.edu.ifpb.es.daw.rest.dto.request.ItemCarrinhoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CarrinhoResponseDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ItemCarrinhoResponseDTO;
import br.edu.ifpb.es.daw.service.CarrinhoService;
import br.edu.ifpb.es.daw.service.ItemCarrinhoService;
import br.edu.ifpb.es.daw.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/itens-carrinho")
public class ItemCarrinhoRestController implements ItemCarrinhoRestControllerApi {

    @Autowired private ItemCarrinhoMapper mapper;
    @Autowired private CarrinhoMapper carrinhoMapper;
    @Autowired private ItemCarrinhoService service;
    @Autowired private CarrinhoService carrinhoService;
    @Autowired private ProdutoService produtoService;

    @Override @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<ItemCarrinhoResponseDTO>> listar(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarTodos(page).map(mapper::from));
    }

    @Override @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENTE')")
    public ResponseEntity<ItemCarrinhoResponseDTO> adicionar(@RequestBody @Valid ItemCarrinhoRequestDTO dto) {
        Carrinho carrinho = carrinhoService.buscarPorId(dto.getIdCarrinho());
        return new ResponseEntity<>(mapper.from(service.adicionar(
                carrinho, produtoService.buscarPorId(dto.getIdProduto()), dto.getQuantidade())), HttpStatus.CREATED);
    }

    @Override @DeleteMapping("/{idCarrinho}/{idProduto}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENTE')")
    public ResponseEntity<CarrinhoResponseDTO> remover(@PathVariable Long idCarrinho, @PathVariable Long idProduto) {
        Carrinho carrinho = carrinhoService.buscarPorId(idCarrinho);
        return ResponseEntity.ok(carrinhoMapper.from(service.remover(carrinho, idProduto)));
    }
}
