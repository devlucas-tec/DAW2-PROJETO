package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.ConflitoException;
import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.model.Categoria;
import br.edu.ifpb.es.daw.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class CategoriaService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private CategoriaRepository repository;

    @Transactional
    public Categoria criar(Categoria obj) {
        validarNomeDisponivel(obj.getNome(), null);
        return repository.save(obj);
    }

    public Page<Categoria> recuperarTodos(int page) {
        return repository.findAll(PageRequest.of(page, PAGE_SIZE));
    }

    public Categoria buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Categoria", id));
    }

    @Transactional
    public Categoria atualizar(Long id, Categoria dados) {
        Categoria categoria = buscarPorId(id);
        validarNomeDisponivel(dados.getNome(), id);
        categoria.setNome(dados.getNome());
        categoria.setDescricao(dados.getDescricao());
        return repository.save(categoria);
    }

    @Transactional
    public void remover(Long id) {
        repository.delete(buscarPorId(id));
        repository.flush();
    }

    private void validarNomeDisponivel(String nome, Long idAtual) {
        repository.findByNomeIgnoreCase(nome)
                .filter(existente -> !Objects.equals(existente.getId(), idAtual))
                .ifPresent(existente -> {
                    throw new ConflitoException("Já existe uma categoria com o nome '" + nome + "'.");
                });
    }
}
