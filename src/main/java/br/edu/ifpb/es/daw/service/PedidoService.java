package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import br.edu.ifpb.es.daw.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Transactional
    public Pedido criar(Pedido obj) {

        return repository.save(obj);
    }

    public List<Pedido> recuperarTodos() {

        return repository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {

        return repository.findById(id);
    }

    public List<Pedido> buscarPorCliente(Cliente cliente) {
        return repository.buscarPorCliente(cliente);
    }

    @Transactional
    public Pedido fechar(Pedido obj) {
        repository.atualizarStatus(obj.getId(), StatusPedido.CONFIRMADO);
        return repository.findById(obj.getId()).get();
    }

    @Transactional
    public Pedido cancelar(Pedido obj) {
        repository.atualizarStatus(obj.getId(), StatusPedido.CANCELADO);
        return repository.findById(obj.getId()).get();
    }

    @Transactional
    public void remover(Pedido obj) {
        repository.delete(obj);
    }
}
