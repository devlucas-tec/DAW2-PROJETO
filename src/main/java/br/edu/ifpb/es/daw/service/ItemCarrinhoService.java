package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.exception.EstadoInvalidoException;
import br.edu.ifpb.es.daw.model.Carrinho;
import br.edu.ifpb.es.daw.model.ItemCarrinho;
import br.edu.ifpb.es.daw.model.ItemCarrinhoId;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.repository.CarrinhoRepository;
import br.edu.ifpb.es.daw.repository.ItemCarrinhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemCarrinhoService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private ItemCarrinhoRepository repository;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    public Page<ItemCarrinho> recuperarTodos(int page) {
        return repository.findAll(PageRequest.of(page, PAGE_SIZE));
    }

    @Transactional
    public ItemCarrinho adicionar(Carrinho carrinho, Produto produto, int quantidade) {
        if (quantidade > produto.getEstoque()) {
            throw new EstadoInvalidoException(
                    "Estoque insuficiente para o produto '" + produto.getNome() + "'. Disponível: " + produto.getEstoque());
        }
        ItemCarrinhoId id = new ItemCarrinhoId(carrinho.getId(), produto.getId());
        ItemCarrinho item = carrinho.getItens().stream()
                .filter(existente -> existente.getId().equals(id))
                .findFirst()
                .orElseGet(() -> {
                    ItemCarrinho novo = new ItemCarrinho();
                    novo.setId(id);
                    novo.setCarrinho(carrinho);
                    novo.setProduto(produto);
                    carrinho.getItens().add(novo);
                    return novo;
                });
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());
        carrinhoRepository.save(carrinho);
        return item;
    }

    @Transactional
    public Carrinho remover(Carrinho carrinho, Long idProduto) {
        ItemCarrinho item = carrinho.getItens().stream()
                .filter(existente -> existente.getProduto().getId().equals(idProduto))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("ItemCarrinho", "produto", String.valueOf(idProduto)));
        carrinho.getItens().remove(item);
        return carrinhoRepository.save(carrinho);
    }
}
