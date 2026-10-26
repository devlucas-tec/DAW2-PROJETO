package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.repository.ClienteRepository;
import br.edu.ifpb.es.daw.rest.dto.request.ClienteUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticado;

    @Transactional
    public Cliente criar(Cliente obj) {
        usuarioService.validarEmailDisponivel(obj.getEmail(), null);
        obj.setSenha(usuarioService.codificarSenha(obj.getSenha()));
        obj.setRole(Role.CLIENTE);
        return repository.save(obj);
    }

    public Page<Cliente> recuperarTodos(int page) {
        return repository.findAll(PageRequest.of(page, PAGE_SIZE));
    }

    public Page<Cliente> filtrar(String nome, String email, int page) {
        return repository.filtrar(
                nome != null && nome.isBlank() ? null : nome,
                email != null && email.isBlank() ? null : email,
                PageRequest.of(page, PAGE_SIZE)
        );
    }

    public Cliente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente", id));
    }

    public Cliente buscarComAcesso(Long id) {
        usuarioAutenticado.validarProprioUsuario(id);
        return buscarPorId(id);
    }

    @Transactional
    public Cliente atualizar(Long id, ClienteUpdateDTO dto) {
        Cliente cliente = buscarComAcesso(id);
        usuarioService.atualizarDadosBasicos(cliente, dto.getNome(), dto.getEmail(), dto.getSenha());
        if (dto.getTelefone() != null) {
            cliente.setTelefone(dto.getTelefone());
        }
        return repository.save(cliente);
    }

    @Transactional
    public void remover(Long id) {
        repository.delete(buscarPorId(id));
        repository.flush();
    }
}
