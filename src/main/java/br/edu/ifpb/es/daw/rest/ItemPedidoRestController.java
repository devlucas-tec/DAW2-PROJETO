package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.mapper.ItemPedidoMapper;
import br.edu.ifpb.es.daw.mapper.PedidoMapper;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.rest.dto.request.ItemPedidoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ItemPedidoResponseDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PedidoResponseDTO;
import br.edu.ifpb.es.daw.service.ItemPedidoService;
import br.edu.ifpb.es.daw.service.PedidoService;
import br.edu.ifpb.es.daw.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/itens-pedido")
public class ItemPedidoRestController implements ItemPedidoRestControllerApi {

    @Autowired private ItemPedidoMapper mapper;
    @Autowired private PedidoMapper pedidoMapper;
    @Autowired private ItemPedidoService service;
    @Autowired private PedidoService pedidoService;
    @Autowired private ProdutoService produtoService;

    @Override @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<ItemPedidoResponseDTO>> listar(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarTodos(page).map(mapper::from));
    }

    @Override @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENTE')")
    public ResponseEntity<ItemPedidoResponseDTO> adicionar(@RequestBody @Valid ItemPedidoRequestDTO dto) {
        Pedido pedido = pedidoService.buscarComoDono(dto.getIdPedido());
        return new ResponseEntity<>(mapper.from(service.adicionar(
                pedido, produtoService.buscarPorId(dto.getIdProduto()), dto.getQuantidade())), HttpStatus.CREATED);
    }

    @Override @DeleteMapping("/{idPedido}/{idProduto}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENTE')")
    public ResponseEntity<PedidoResponseDTO> remover(@PathVariable Long idPedido, @PathVariable Long idProduto) {
        Pedido pedido = pedidoService.buscarComoDono(idPedido);
        return ResponseEntity.ok(pedidoMapper.from(service.remover(pedido, idProduto)));
    }
}
