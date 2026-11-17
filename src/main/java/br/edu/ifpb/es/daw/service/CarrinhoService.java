package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.ConflitoException;
import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.model.Carrinho;
import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.ItemCarrinho;
import br.edu.ifpb.es.daw.repository.CarrinhoRepository;
import br.edu.ifpb.es.daw.repository.ClienteRepository;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.rest.dto.request.ItemMesclarCarrinhoDTO;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarrinhoService {

    @Autowired
    private br.edu.ifpb.es.daw.repository.ProdutoRepository produtoRepository;

    @Autowired
    private CarrinhoRepository repository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ItemCarrinhoService itemCarrinhoService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticado;

    @Transactional
    public Carrinho criar(Cliente cliente) {
        if (repository.existsByCliente(cliente)) {
            throw new ConflitoException("O cliente já possui um carrinho.");
        }
        Carrinho carrinho = new Carrinho();
        carrinho.setCliente(cliente);
        return repository.save(carrinho);
    }

    public Carrinho buscarPorId(Long id) {
        Carrinho carrinho = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Carrinho", id));
        usuarioAutenticado.validarProprioUsuario(carrinho.getCliente().getId());
        return carrinho;
    }

    @Transactional
    public Carrinho limpar(Long id) {
        Carrinho carrinho = buscarPorId(id);
        carrinho.getItens().clear();
        return repository.save(carrinho);
    }

    // ---------- Carrinho do cliente logado ----------

    /** Retorna o carrinho do cliente logado, criando-o se ainda não existir. Os preços são atualizados com os atuais. */
    @Transactional
    public Carrinho meuCarrinho() {
        Cliente cliente = clienteLogado();
        Carrinho carrinho = repository.findByClienteId(cliente.getId()).orElseGet(() -> {
            Carrinho novo = new Carrinho();
            novo.setCliente(cliente);
            return repository.save(novo);
        });
        for (ItemCarrinho item : carrinho.getItens()) {
            item.setPrecoUnitario(item.getProduto().getPreco());
        }
        return carrinho;
    }

    @Transactional
    public Carrinho adicionarItem(Long idProduto, int quantidade) {
        Carrinho carrinho = meuCarrinho();
        itemCarrinhoService.salvarItem(carrinho, produtoService.buscarPorId(idProduto), quantidade, true);
        return carrinho;
    }

    @Transactional
    public Carrinho definirQuantidade(Long idProduto, int quantidade) {
        Carrinho carrinho = meuCarrinho();
        itemCarrinhoService.salvarItem(carrinho, produtoService.buscarPorId(idProduto), quantidade, false);
        return carrinho;
    }

    @Transactional
    public Carrinho removerItem(Long idProduto) {
        return itemCarrinhoService.remover(meuCarrinho(), idProduto);
    }

    /**
     * Junta o carrinho de visitante (montado no localStorage) ao carrinho do cliente depois do login.
     * As quantidades são somadas e limitadas ao estoque; itens indisponíveis, inexistentes ou do próprio
     * cliente são ignorados em silêncio. Devolve o carrinho final.
     */
    @Transactional
    public Carrinho mesclar(List<ItemMesclarCarrinhoDTO> itens) {
        Carrinho carrinho = meuCarrinho();
        for (ItemMesclarCarrinhoDTO pedido : itens) {
            Produto produto = produtoRepository.findById(pedido.getIdProduto()).orElse(null);
            if (produto == null || !produto.isDisponivelParaVenda() || produto.getEstoque() < 1
                    || Objects.equals(produto.getVendedor().getId(), carrinho.getCliente().getId())) {
                continue;
            }
            int atual = carrinho.getItens().stream()
                    .filter(i -> Objects.equals(i.getProduto().getId(), produto.getId()))
                    .mapToInt(ItemCarrinho::getQuantidade)
                    .findFirst().orElse(0);
            int quantidade = Math.min(atual + pedido.getQuantidade(), produto.getEstoque());
            itemCarrinhoService.salvarItem(carrinho, produto, quantidade, false);
        }
        return carrinho;
    }

    @Transactional
    public Carrinho esvaziarMeuCarrinho() {
        Carrinho carrinho = meuCarrinho();
        carrinho.getItens().clear();
        return repository.save(carrinho);
    }

    private Cliente clienteLogado() {
        Long id = usuarioAutenticado.getUsuario().getId();
        clienteRepository.travarParaAtualizacao(id);
        return clienteRepository.findById(id)
                .orElseThrow(() -> new AccessDeniedException("Apenas clientes possuem carrinho."));
    }
}
