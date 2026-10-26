package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Endereco;
import br.edu.ifpb.es.daw.model.Usuario;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnderecoService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private EnderecoRepository repository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticado;

    @Transactional
    public Endereco criar(Endereco obj, Cliente cliente) {
        obj.setCliente(cliente);
        obj.setEstado(obj.getEstado().toUpperCase());
        return repository.save(obj);
    }

    public Page<Endereco> recuperarTodos(int page) {
        Usuario usuario = usuarioAutenticado.getUsuario();
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        if (usuario.getRole() == Role.ADMIN) {
            return repository.findAll(pageable);
        }
        return repository.buscarPorCliente(usuario.getId(), pageable);
    }

    public Endereco buscarPorId(Long id) {
        Endereco endereco = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Endereco", id));
        usuarioAutenticado.validarProprioUsuario(endereco.getCliente().getId());
        return endereco;
    }

    @Transactional
    public Endereco atualizar(Endereco endereco) {
        endereco.setEstado(endereco.getEstado().toUpperCase());
        return repository.save(endereco);
    }

    @Transactional
    public void remover(Long id) {
        repository.delete(buscarPorId(id));
    }
}
