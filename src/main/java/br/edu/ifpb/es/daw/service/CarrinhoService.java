package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Carrinho;
import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.ItemCarrinho;
import br.edu.ifpb.es.daw.repository.CarrinhoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarrinhoService {

    @Autowired
    private CarrinhoRepository repository;

    @Transactional
    public Carrinho criar(Carrinho obj) {

        return repository.save(obj);
    }

    public Optional<Carrinho> buscarPorId(Long id) {

        return repository.findById(id);
    }

    public Optional<Carrinho> buscarPorCliente(Cliente cliente) {
        return repository.buscarPorCliente(cliente);
    }

    @Transactional
    public Carrinho adicionarItem(Carrinho carrinho, ItemCarrinho item) {
        item.setCarrinho(carrinho);
        carrinho.getItens().add(item);
        return repository.save(carrinho);
    }

    @Transactional
    public Carrinho removerItem(Carrinho carrinho, ItemCarrinho item) {
        carrinho.getItens().remove(item);
        return repository.save(carrinho);
    }

    @Transactional
    public Carrinho limpar(Carrinho carrinho) {
        carrinho.getItens().clear();
        return repository.save(carrinho);
    }
}
