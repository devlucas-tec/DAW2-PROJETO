package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import br.edu.ifpb.es.daw.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private PedidoRepository repository;

    @Transactional
    public Pedido criar(Pedido obj) {
        return repository.save(obj);
    }

    public Page<Pedido> recuperarTodos(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return repository.findAll(pageable);
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
