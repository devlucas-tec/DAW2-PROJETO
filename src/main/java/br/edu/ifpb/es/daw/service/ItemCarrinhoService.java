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

import java.util.Objects;
import java.util.Optional;

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

    /** Define a quantidade do produto no carrinho (substitui a anterior). */
    @Transactional
    public ItemCarrinho adicionar(Carrinho carrinho, Produto produto, int quantidade) {
        return salvarItem(carrinho, produto, quantidade, false);
    }

    /**
     * Adiciona o produto ao carrinho. Se {@code somar} for true e o produto já estiver no carrinho,
     * a quantidade é somada à existente; caso contrário, é substituída.
     */
    @Transactional
    public ItemCarrinho salvarItem(Carrinho carrinho, Produto produto, int quantidade, boolean somar) {
        if (quantidade < 1) {
            throw new IllegalArgumentException("A quantidade deve ser pelo menos 1.");
        }
        if (!produto.isDisponivelParaVenda()) {
            throw new EstadoInvalidoException("O produto '" + produto.getNome() + "' não está disponível para venda.");
        }
        if (Objects.equals(produto.getVendedor().getId(), carrinho.getCliente().getId())) {
            throw new EstadoInvalidoException("Você não pode comprar o seu próprio produto.");
        }

        Optional<ItemCarrinho> existente = buscarItem(carrinho, produto.getId());
        int quantidadeFinal = somar && existente.isPresent() ? existente.get().getQuantidade() + quantidade : quantidade;
        if (quantidadeFinal > produto.getEstoque()) {
            throw new EstadoInvalidoException("Estoque insuficiente para o produto '" + produto.getNome()
                    + "'. Disponível: " + produto.getEstoque() + ", quantidade no carrinho solicitada: " + quantidadeFinal + ".");
        }

        ItemCarrinho item = existente.orElseGet(() -> {
            ItemCarrinho novo = new ItemCarrinho();
            novo.setId(new ItemCarrinhoId(carrinho.getId(), produto.getId()));
            novo.setCarrinho(carrinho);
            novo.setProduto(produto);
            carrinho.getItens().add(novo);
            return novo;
        });
        item.setQuantidade(quantidadeFinal);
        item.setPrecoUnitario(produto.getPreco());
        carrinho.setDataAtualizacao(java.time.LocalDate.now());
        carrinhoRepository.save(carrinho);
        return item;
    }

    @Transactional
    public Carrinho remover(Carrinho carrinho, Long idProduto) {
        ItemCarrinho item = buscarItem(carrinho, idProduto)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("ItemCarrinho", "produto", String.valueOf(idProduto)));
        carrinho.getItens().remove(item);
        return carrinhoRepository.save(carrinho);
    }

    private Optional<ItemCarrinho> buscarItem(Carrinho carrinho, Long idProduto) {
        return carrinho.getItens().stream()
                .filter(existente -> existente.getProduto().getId().equals(idProduto))
                .findFirst();
    }
}
