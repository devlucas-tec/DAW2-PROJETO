package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.ConflitoException;
import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.exception.EstadoInvalidoException;
import br.edu.ifpb.es.daw.model.Avaliacao;
import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.repository.AvaliacaoRepository;
import br.edu.ifpb.es.daw.repository.PedidoRepository;
import br.edu.ifpb.es.daw.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class AvaliacaoService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private AvaliacaoRepository repository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticado;

    @Transactional
    public Avaliacao criar(Avaliacao obj, Cliente cliente, Produto produto) {
        if (!pedidoRepository.clienteRecebeuProduto(cliente.getId(), produto.getId())) {
            throw new EstadoInvalidoException("O cliente só pode avaliar produtos de pedidos já ENTREGUES.");
        }
        if (repository.existsByClienteAndProduto(cliente, produto)) {
            throw new ConflitoException("O cliente já avaliou este produto.");
        }
        obj.setCliente(cliente);
        obj.setProduto(produto);
        Avaliacao salva = repository.save(obj);
        atualizarResumoDoProduto(produto);
        return salva;
    }

    public Page<Avaliacao> recuperarTodos(int page) {
        if (!usuarioAutenticado.isAdmin()) {
            throw new AccessDeniedException("Informe o parâmetro 'idProduto'. A listagem geral é restrita ao ADMIN.");
        }
        return repository.findAll(paginaMaisRecentes(page));
    }

    /** Avaliações de um produto (público), mais recentes primeiro. */
    public Page<Avaliacao> recuperarPorProduto(Long idProduto, int page) {
        if (!produtoRepository.existsById(idProduto)) {
            throw new EntidadeNaoEncontradaException("Produto", idProduto);
        }
        return repository.findByProdutoId(idProduto, paginaMaisRecentes(page));
    }

    public Page<Avaliacao> recuperarMinhas(int page) {
        return repository.findByClienteId(usuarioAutenticado.getUsuario().getId(), paginaMaisRecentes(page));
    }

    public Avaliacao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Avaliacao", id));
    }

    /** O próprio autor ou o ADMIN (moderação) podem remover. */
    @Transactional
    public void remover(Long id) {
        Avaliacao avaliacao = buscarPorId(id);
        usuarioAutenticado.validarProprioUsuario(avaliacao.getCliente().getId());
        Produto produto = avaliacao.getProduto();
        repository.delete(avaliacao);
        repository.flush();
        atualizarResumoDoProduto(produto);
    }

    /** Recalcula a média (uma casa decimal) e o total de avaliações guardados no produto. */
    public void atualizarResumoDoProduto(Produto produto) {
        long total = repository.countByProdutoId(produto.getId());
        Double media = repository.mediaPorProduto(produto.getId());
        produto.setTotalAvaliacoes((int) total);
        produto.setMediaAvaliacoes(total == 0 || media == null ? 0.0 : arredondar(media));
        produtoRepository.save(produto);
    }

    public static double arredondar(double valor) {
        return BigDecimal.valueOf(valor).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    private Pageable paginaMaisRecentes(int page) {
        return PageRequest.of(Math.max(page, 0), PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, "dataAvaliacao").and(Sort.by(Sort.Direction.DESC, "id")));
    }
}
