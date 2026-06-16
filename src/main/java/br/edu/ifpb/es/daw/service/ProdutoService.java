package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProdutoService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private ProdutoRepository repository;

    @Transactional
    public Produto criar(Produto obj) {
        return repository.save(obj);
    }

    // listagem simples com paginação (mantida para compatibilidade)
    public Page<Produto> recuperarTodos(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return repository.findAll(pageable);
    }

    // listagem com filtros + paginação
    public Page<Produto> filtrar(String nome, Long idCategoria,
                                 BigDecimal precoMin, BigDecimal precoMax, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return repository.filtrar(
                nome != null && nome.isBlank() ? null : nome,
                idCategoria,
                precoMin,
                precoMax,
                pageable
        );
    }

    public Optional<Produto> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Produto atualizar(Produto obj) {
        return repository.save(obj);
    }

    @Transactional
    public void remover(Produto obj) {
        repository.delete(obj);
    }
}