package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.model.Categoria;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.model.Usuario;
import br.edu.ifpb.es.daw.model.Vendedor;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.repository.ProdutoRepository;
import br.edu.ifpb.es.daw.rest.dto.request.ProdutoUpdateDTO;
import br.edu.ifpb.es.daw.exception.ConflitoException;
import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class ProdutoService {

    private static final int PAGE_SIZE = 10;
    private static final int PAGE_SIZE_MAXIMO = 50;

    /** Valores aceitos no parâmetro "sort" da busca. */
    public static final String ORDENACOES_ACEITAS =
            "preco,asc | preco,desc | nome,asc | nome,desc | dataCadastro,desc | dataCadastro,asc | avaliacao,desc";

    @Autowired private br.edu.ifpb.es.daw.repository.ItemPedidoRepository itemPedidoRepository;
    @Autowired private br.edu.ifpb.es.daw.repository.ItemCarrinhoRepository itemCarrinhoRepository;
    @Autowired private br.edu.ifpb.es.daw.repository.FavoritoRepository favoritoRepository;

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticado;

    @Transactional
    public Produto criar(Produto obj, Categoria categoria, Vendedor vendedor) {
        obj.setCategoria(categoria);
        obj.setVendedor(vendedor);
        return repository.save(obj);
    }

    /** Vitrine pública: ignora produtos inativos, sem estoque (por padrão) e de vendedores bloqueados. */
    public Page<Produto> buscarVitrine(String nome, Long idCategoria, Long idVendedor,
                                       BigDecimal precoMin, BigDecimal precoMax,
                                       Boolean somenteComEstoque, String sort, int page, Integer size) {
        return buscarVitrine(nome, idCategoria, idVendedor, precoMin, precoMax, null, somenteComEstoque, sort, page, size);
    }

    /** Vitrine com filtro de avaliação mínima (média de 0 a 5). */
    public Page<Produto> buscarVitrine(String nome, Long idCategoria, Long idVendedor,
                                       BigDecimal precoMin, BigDecimal precoMax, Double avaliacaoMin,
                                       Boolean somenteComEstoque, String sort, int page, Integer size) {
        if (precoMin != null && precoMax != null && precoMin.compareTo(precoMax) > 0) {
            throw new IllegalArgumentException("O preço mínimo não pode ser maior que o preço máximo.");
        }
        if (avaliacaoMin != null && (avaliacaoMin < 0 || avaliacaoMin > 5)) {
            throw new IllegalArgumentException("A avaliação mínima deve estar entre 0 e 5.");
        }
        return repository.buscarVitrine(
                vazioParaNull(nome),
                idCategoria,
                idVendedor,
                precoMin,
                precoMax,
                avaliacaoMin,
                somenteComEstoque == null || somenteComEstoque,
                PageRequest.of(Math.max(page, 0), tamanhoPagina(size), ordenacao(sort))
        );
    }

    /** Produtos do vendedor logado, inclusive inativos e sem estoque. */
    public Page<Produto> buscarMeus(String nome, Boolean ativo, int page, Integer size) {
        Usuario usuario = usuarioAutenticado.getUsuario();
        return repository.buscarDoVendedor(usuario.getId(), vazioParaNull(nome), ativo,
                PageRequest.of(Math.max(page, 0), tamanhoPagina(size),
                        Sort.by(Sort.Direction.DESC, "dataCadastro").and(Sort.by("id"))));
    }

    /** Painel do admin: todos os produtos, com filtro por loja, nome e status (ativo/pausado). */
    public Page<Produto> buscarTodos(Long idVendedor, String nome, Boolean ativo, int page, Integer size) {
        return repository.buscarTodos(idVendedor, vazioParaNull(nome), ativo,
                PageRequest.of(Math.max(page, 0), tamanhoPagina(size),
                        Sort.by(Sort.Direction.DESC, "dataCadastro").and(Sort.by("id"))));
    }

    /**
     * Mais vendidos (soma das quantidades em pedidos não cancelados). Se houver poucas vendas,
     * completa com os mais bem avaliados, para a vitrine nunca ficar vazia.
     */
    public List<Produto> maisVendidos(Long idCategoria, Integer limite) {
        int quantidade = limite == null ? 10 : Math.min(Math.max(limite, 1), 20);
        List<Long> ids = itemPedidoRepository.idsMaisVendidos(
                List.of(StatusPedido.CANCELADO, StatusPedido.DEVOLVIDO), idCategoria, PageRequest.of(0, quantidade));
        Map<Long, Produto> porId = repository.findAllById(ids).stream()
                .collect(Collectors.toMap(Produto::getId, Function.identity()));
        List<Produto> resultado = new ArrayList<>(ids.stream().map(porId::get).filter(Objects::nonNull).toList());
        if (resultado.size() < quantidade) {
            Set<Long> jaIncluidos = new HashSet<>(ids);
            buscarVitrine(null, idCategoria, null, null, null, null, true, "avaliacao,desc", 0, PAGE_SIZE_MAXIMO)
                    .stream()
                    .filter(p -> !jaIncluidos.contains(p.getId()))
                    .limit(quantidade - resultado.size())
                    .forEach(resultado::add);
        }
        return resultado;
    }

    public Produto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto", id));
    }

    /**
     * Detalhe do produto na vitrine. Um anúncio pausado (ou de vendedor bloqueado) só é visível
     * para o próprio vendedor e para o ADMIN; para os demais ele "não existe" (404).
     */
    public Produto buscarVisivel(Long id) {
        Produto produto = buscarPorId(id);
        if (!produto.isDisponivelParaVenda() && !podeGerenciar(produto)) {
            throw new EntidadeNaoEncontradaException("Produto", id);
        }
        return produto;
    }

    @Transactional
    public Produto atualizar(Long id, ProdutoUpdateDTO dados, Categoria categoria) {
        Produto produto = buscarPorId(id);
        validarDono(produto);
        if (dados.getNome() != null) produto.setNome(dados.getNome());
        if (dados.getDescricao() != null) produto.setDescricao(dados.getDescricao());
        if (dados.getPreco() != null) produto.setPreco(dados.getPreco());
        if (dados.getEstoque() != null) produto.setEstoque(dados.getEstoque());
        if (dados.getImagemUrl() != null) produto.setImagemUrl(dados.getImagemUrl());
        if (dados.getImagens() != null) {
            produto.getImagens().clear();
            produto.getImagens().addAll(new ArrayList<>(dados.getImagens()));
        }
        if (dados.getAtivo() != null) produto.setAtivo(dados.getAtivo());
        if (categoria != null) produto.setCategoria(categoria);
        return repository.save(produto);
    }

    @Transactional
    public void remover(Long id) {
        Produto produto = buscarPorId(id);
        validarDono(produto);
        if (itemPedidoRepository.existsByProdutoId(id)) {
            throw new ConflitoException("Este produto já foi vendido e aparece em pedidos de clientes. "
                    + "Pause o anúncio (ativo=false) em vez de excluí-lo.");
        }
        itemCarrinhoRepository.removerProdutosDosCarrinhos(List.of(id));
        favoritoRepository.removerDosProdutos(List.of(id));
        repository.delete(produto);
        repository.flush();
    }

    private void validarDono(Produto produto) {
        if (!podeGerenciar(produto)) {
            throw new AccessDeniedException("Você só pode alterar os seus próprios produtos.");
        }
    }

    private boolean podeGerenciar(Produto produto) {
        return usuarioAutenticado.getUsuarioOpcional()
                .map(usuario -> usuario.getRole() == Role.ADMIN
                        || Objects.equals(produto.getVendedor().getId(), usuario.getId()))
                .orElse(false);
    }

    private Sort ordenacao(String sort) {
        Sort desempate = Sort.by(Sort.Direction.ASC, "id");
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "dataCadastro").and(desempate);
        }
        Sort escolhida = switch (sort.replace(" ", "")) {
            case "preco,asc", "preco" -> Sort.by(Sort.Direction.ASC, "preco");
            case "preco,desc" -> Sort.by(Sort.Direction.DESC, "preco");
            case "nome,asc", "nome" -> Sort.by(Sort.Direction.ASC, "nome");
            case "nome,desc" -> Sort.by(Sort.Direction.DESC, "nome");
            case "dataCadastro,desc", "dataCadastro" -> Sort.by(Sort.Direction.DESC, "dataCadastro");
            case "dataCadastro,asc" -> Sort.by(Sort.Direction.ASC, "dataCadastro");
            case "avaliacao,desc", "avaliacao" -> Sort.by(Sort.Direction.DESC, "mediaAvaliacoes", "totalAvaliacoes");
            default -> throw new IllegalArgumentException(
                    "Ordenação '" + sort + "' inválida. Valores aceitos: " + ORDENACOES_ACEITAS);
        };
        return escolhida.and(desempate);
    }

    private int tamanhoPagina(Integer size) {
        if (size == null) {
            return PAGE_SIZE;
        }
        return Math.min(Math.max(size, 1), PAGE_SIZE_MAXIMO);
    }

    private String vazioParaNull(String texto) {
        return texto != null && texto.isBlank() ? null : texto;
    }
}
