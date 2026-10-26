package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.model.Categoria;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.model.Usuario;
import br.edu.ifpb.es.daw.model.Vendedor;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class ProdutoService {

    private static final int PAGE_SIZE = 10;

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

    public Page<Produto> recuperarTodos(int page) {
        return repository.findAll(PageRequest.of(page, PAGE_SIZE));
    }

    public Page<Produto> filtrar(String nome, Long idCategoria,
                                 BigDecimal precoMin, BigDecimal precoMax, int page) {
        if (precoMin != null && precoMax != null && precoMin.compareTo(precoMax) > 0) {
            throw new IllegalArgumentException("O preço mínimo não pode ser maior que o preço máximo.");
        }
        return repository.filtrar(
                nome != null && nome.isBlank() ? null : nome,
                idCategoria,
                precoMin,
                precoMax,
                PageRequest.of(page, PAGE_SIZE)
        );
    }

    public Produto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto", id));
    }

    @Transactional
    public Produto atualizar(Long id, Produto dados, Categoria categoria) {
        Produto produto = buscarPorId(id);
        validarDono(produto);
        produto.setNome(dados.getNome());
        produto.setDescricao(dados.getDescricao());
        produto.setPreco(dados.getPreco());
        produto.setEstoque(dados.getEstoque());
        produto.setCategoria(categoria);
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
        Usuario usuario = usuarioAutenticado.getUsuario();
        if (usuario.getRole() != Role.ADMIN && !Objects.equals(produto.getVendedor().getId(), usuario.getId())) {
            throw new AccessDeniedException("Você só pode alterar os seus próprios produtos.");
        }
    }
}
