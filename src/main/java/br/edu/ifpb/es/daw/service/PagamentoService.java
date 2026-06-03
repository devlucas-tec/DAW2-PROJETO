package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Pagamento;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.StatusPagamento;
import br.edu.ifpb.es.daw.repository.PagamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    @Transactional
    public Pagamento criar(Pagamento obj) {

        return repository.save(obj);
    }

    public Optional<Pagamento> buscarPorId(Long id) {

        return repository.findById(id);
    }

    public Optional<Pagamento> buscarPorPedido(Pedido pedido) {
        return repository.buscarPorPedido(pedido);
    }

    @Transactional
    public Pagamento confirmar(Pagamento obj) {
        repository.atualizarStatus(obj.getId(), StatusPagamento.APROVADO);
        return repository.findById(obj.getId()).get();
    }

    @Transactional
    public Pagamento cancelar(Pagamento obj) {
        repository.atualizarStatus(obj.getId(), StatusPagamento.RECUSADO);
        return repository.findById(obj.getId()).get();
    }
}
