package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Pagamento;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    @Query("SELECT p FROM Pagamento p WHERE p.pedido = :pedido")
    Optional<Pagamento> buscarPorPedido(Pedido pedido);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Pagamento p SET p.status = :status WHERE p.id = :id")
    int atualizarStatus(Long id, StatusPagamento status);
}
