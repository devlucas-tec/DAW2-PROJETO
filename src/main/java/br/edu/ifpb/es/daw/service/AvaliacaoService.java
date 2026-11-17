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
import br.edu.ifpb.es.daw.rest.dto.response.AvaliacaoResumoDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PodeAvaliarDTO;
import java.util.LinkedHashMap;
import java.util.Map;
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

    /** Avaliações dos produtos do vendedor logado, mais recentes primeiro. */
    public Page<Avaliacao> recuperarRecebidas(int page) {
        return repository.findByProdutoVendedorId(usuarioAutenticado.getUsuario().getId(), paginaMaisRecentes(page));
    }

    /** Média, total e quantidade de avaliações por nota (5 a 1) de um produto. */
    public AvaliacaoResumoDTO resumo(Long idProduto) {
        if (!produtoRepository.existsById(idProduto)) {
            throw new EntidadeNaoEncontradaException("Produto", idProduto);
        }
        Map<Integer, Long> distribuicao = new LinkedHashMap<>();
        for (int nota = 5; nota >= 1; nota--) {
            distribuicao.put(nota, 0L);
        }
        long total = 0;
        long soma = 0;
        for (Object[] linha : repository.contarPorNota(idProduto)) {
            int nota = ((Number) linha[0]).intValue();
            long quantidade = ((Number) linha[1]).longValue();
            distribuicao.put(nota, quantidade);
            total += quantidade;
            soma += nota * quantidade;
        }
        Double media = total == 0 ? null : arredondar((double) soma / total);
        return new AvaliacaoResumoDTO(idProduto, media, total, distribuicao);
    }

    /** Se o cliente logado pode avaliar o produto (recebeu o produto e ainda não avaliou). */
    public PodeAvaliarDTO podeAvaliar(Long idProduto) {
        Long idCliente = usuarioAutenticado.getUsuario().getId();
        if (!produtoRepository.existsById(idProduto)) {
            throw new EntidadeNaoEncontradaException("Produto", idProduto);
        }
        if (repository.existsByClienteIdAndProdutoId(idCliente, idProduto)) {
            return new PodeAvaliarDTO(false, "Você já avaliou este produto.");
        }
        if (!pedidoRepository.clienteRecebeuProduto(idCliente, idProduto)) {
            return new PodeAvaliarDTO(false, "Só quem recebeu este produto (pedido entregue) pode avaliá-lo.");
        }
        return new PodeAvaliarDTO(true, null);
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
