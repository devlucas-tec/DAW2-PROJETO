package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.model.Categoria;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.model.Usuario;
import br.edu.ifpb.es.daw.model.Vendedor;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.repository.ProdutoRepository;
import br.edu.ifpb.es.daw.rest.dto.request.ProdutoUpdateDTO;
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
        if (precoMin != null && precoMax != null && precoMin.compareTo(precoMax) > 0) {
            throw new IllegalArgumentException("O preço mínimo não pode ser maior que o preço máximo.");
        }
        return repository.buscarVitrine(
                vazioParaNull(nome),
                idCategoria,
                idVendedor,
                precoMin,
                precoMax,
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
