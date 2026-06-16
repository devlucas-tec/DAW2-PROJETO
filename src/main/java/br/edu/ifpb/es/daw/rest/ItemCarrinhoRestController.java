package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.mapper.ItemCarrinhoMapper;
import br.edu.ifpb.es.daw.model.Carrinho;
import br.edu.ifpb.es.daw.model.ItemCarrinho;
import br.edu.ifpb.es.daw.model.ItemCarrinhoId;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.rest.dto.request.ItemCarrinhoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ItemCarrinhoResponseDTO;
import br.edu.ifpb.es.daw.service.CarrinhoService;
import br.edu.ifpb.es.daw.service.ItemCarrinhoService;
import br.edu.ifpb.es.daw.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/itens-carrinho")
public class ItemCarrinhoRestController implements ItemCarrinhoRestControllerApi {

    @Autowired private ItemCarrinhoMapper mapper;
    @Autowired private ItemCarrinhoService service;
    @Autowired private CarrinhoService carrinhoService;
    @Autowired private ProdutoService produtoService;

    @Override @GetMapping
    public ResponseEntity<Page<ItemCarrinhoResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarTodos(page).map(mapper::from));
    }

    @Override @PostMapping
    public ResponseEntity<ItemCarrinhoResponseDTO> adicionar(@RequestBody @Valid ItemCarrinhoRequestDTO dto) {
        ItemCarrinho obj = mapper.from(dto);
        Carrinho carrinho = carrinhoService.buscarPorId(dto.getIdCarrinho())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Carrinho", dto.getIdCarrinho()));
        Produto produto = produtoService.buscarPorId(dto.getIdProduto())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto", dto.getIdProduto()));
        obj.setId(new ItemCarrinhoId(dto.getIdCarrinho(), dto.getIdProduto()));
        obj.setCarrinho(carrinho);
        obj.setProduto(produto);
        return new ResponseEntity<>(mapper.from(service.criar(obj)), HttpStatus.CREATED);
    }
}
