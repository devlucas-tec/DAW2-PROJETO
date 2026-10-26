package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.ConflitoException;
import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.model.Carrinho;
import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.repository.CarrinhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarrinhoService {

    @Autowired
    private CarrinhoRepository repository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticado;

    @Transactional
    public Carrinho criar(Cliente cliente) {
        if (repository.existsByCliente(cliente)) {
            throw new ConflitoException("O cliente já possui um carrinho.");
        }
        Carrinho carrinho = new Carrinho();
        carrinho.setCliente(cliente);
        return repository.save(carrinho);
    }

    public Carrinho buscarPorId(Long id) {
        Carrinho carrinho = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Carrinho", id));
        usuarioAutenticado.validarProprioUsuario(carrinho.getCliente().getId());
        return carrinho;
    }

    @Transactional
    public Carrinho limpar(Long id) {
        Carrinho carrinho = buscarPorId(id);
        carrinho.getItens().clear();
        return repository.save(carrinho);
    }
}
