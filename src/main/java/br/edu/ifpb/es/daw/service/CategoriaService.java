package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Categoria;
import br.edu.ifpb.es.daw.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    @Transactional
    public Categoria criar(Categoria obj) {

        return repository.save(obj);
    }

    public List<Categoria> recuperarTodas() {

        return repository.findAll();
    }

    public Optional<Categoria> buscarPorID(Long id) {

        return repository.findById(id);
    }

    @Transactional
    public Categoria atualizar(Categoria obj) {

        return repository.save(obj);
    }

    @Transactional
    public void remover(Categoria obj) {

        repository.delete(obj);
    }
}
