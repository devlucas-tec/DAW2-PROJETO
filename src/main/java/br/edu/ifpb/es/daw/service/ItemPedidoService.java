package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.exception.EstadoInvalidoException;
import br.edu.ifpb.es.daw.model.ItemPedido;
import br.edu.ifpb.es.daw.model.ItemPedidoId;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import br.edu.ifpb.es.daw.repository.ItemPedidoRepository;
import br.edu.ifpb.es.daw.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemPedidoService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private ItemPedidoRepository repository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoService pedidoService;

    public Page<ItemPedido> recuperarTodos(int page) {
        return repository.findAll(PageRequest.of(page, PAGE_SIZE));
    }

    @Transactional
    public ItemPedido adicionar(Pedido pedido, Produto produto, int quantidade) {
        pedidoService.validarStatus(pedido, StatusPedido.CRIADO, "alterado");
        if (quantidade > produto.getEstoque()) {
            throw new EstadoInvalidoException(
                    "Estoque insuficiente para o produto '" + produto.getNome() + "'. Disponível: " + produto.getEstoque());
        }
        ItemPedidoId id = new ItemPedidoId(pedido.getId(), produto.getId());
        ItemPedido item = pedido.getItens().stream()
                .filter(existente -> existente.getId().equals(id))
                .findFirst()
                .orElseGet(() -> {
                    ItemPedido novo = new ItemPedido();
                    novo.setId(id);
                    novo.setPedido(pedido);
                    novo.setProduto(produto);
                    pedido.getItens().add(novo);
                    return novo;
                });
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());
        pedido.recalcularValorTotal();
        pedidoRepository.save(pedido);
        return item;
    }

    @Transactional
    public Pedido remover(Pedido pedido, Long idProduto) {
        pedidoService.validarStatus(pedido, StatusPedido.CRIADO, "alterado");
        ItemPedido item = pedido.getItens().stream()
                .filter(existente -> existente.getProduto().getId().equals(idProduto))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("ItemPedido", "produto", String.valueOf(idProduto)));
        pedido.getItens().remove(item);
        pedido.recalcularValorTotal();
        return pedidoRepository.save(pedido);
    }
}
