package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.mapper.ItemPedidoMapper;
import br.edu.ifpb.es.daw.model.ItemPedido;
import br.edu.ifpb.es.daw.model.ItemPedidoId;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.rest.dto.request.ItemPedidoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ItemPedidoResponseDTO;
import br.edu.ifpb.es.daw.service.ItemPedidoService;
import br.edu.ifpb.es.daw.service.PedidoService;
import br.edu.ifpb.es.daw.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/itens-pedido")
public class ItemPedidoRestController implements ItemPedidoRestControllerApi {

    @Autowired private ItemPedidoMapper mapper;
    @Autowired private ItemPedidoService service;
    @Autowired private PedidoService pedidoService;
    @Autowired private ProdutoService produtoService;

    @Override @GetMapping
    public ResponseEntity<Page<ItemPedidoResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarTodos(page).map(mapper::from));
    }

    @Override @PostMapping
    public ResponseEntity<ItemPedidoResponseDTO> adicionar(@RequestBody @Valid ItemPedidoRequestDTO dto) {
        ItemPedido obj = mapper.from(dto);
        Pedido pedido = pedidoService.buscarPorId(dto.getIdPedido())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido", dto.getIdPedido()));
        Produto produto = produtoService.buscarPorId(dto.getIdProduto())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto", dto.getIdProduto()));
        obj.setId(new ItemPedidoId(dto.getIdPedido(), dto.getIdProduto()));
        obj.setPedido(pedido);
        obj.setProduto(produto);
        return new ResponseEntity<>(mapper.from(service.criar(obj)), HttpStatus.CREATED);
    }
}
