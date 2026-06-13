package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Entrega;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.StatusEntrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    @Query("SELECT e FROM Entrega e WHERE e.pedido = :pedido")
    Optional<Entrega> buscarPorPedido(Pedido pedido);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Entrega e SET e.statusEntrega = :status WHERE e.id = :id")
    int atualizarStatus(Long id, StatusEntrega status);
}
