package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Pagamento;
import br.edu.ifpb.es.daw.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    Optional<Pagamento> findByPedido(Pedido pedido);
}
