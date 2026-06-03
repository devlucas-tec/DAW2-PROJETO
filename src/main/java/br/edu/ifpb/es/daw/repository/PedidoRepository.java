package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p WHERE p.cliente = :cliente")
    List<Pedido> buscarPorCliente(Cliente cliente);

    @Query("SELECT p FROM Pedido p WHERE p.status = :status")
    List<Pedido> buscarPorStatus(StatusPedido status);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Pedido p SET p.status = :status WHERE p.id = :id")
    int atualizarStatus(Long id, StatusPedido status);
}
