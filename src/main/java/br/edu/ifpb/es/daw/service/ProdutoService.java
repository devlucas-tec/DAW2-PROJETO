package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Categoria;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @Transactional
    public Produto criar(Produto obj) {

        return repository.save(obj);
    }

    public List<Produto> recuperarTodos() {

        return repository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) {

        return repository.findById(id);
    }

    public List<Produto> buscarPorCategoria(Categoria categoria) {
        return repository.buscarPorCategoria(categoria);
    }

    public List<Produto> buscarPorNome(String nome) {
        return repository.buscarPorNome(nome);
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
