package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Devolucao;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.StatusDevolucao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DevolucaoRepository extends JpaRepository<Devolucao, Long> {

    @Query("SELECT d FROM Devolucao d WHERE d.pedido = :pedido")
    Optional<Devolucao> buscarPorPedido(Pedido pedido);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Devolucao d SET d.status = :status WHERE d.id = :id")
    int atualizarStatus(Long id, StatusDevolucao status);
}
