package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Devolucao;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.StatusDevolucao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

@Repository
public interface DevolucaoRepository extends JpaRepository<Devolucao, Long> {

    boolean existsByPedidoAndStatusIn(Pedido pedido, Collection<StatusDevolucao> status);

    Page<Devolucao> findByPedidoClienteId(Long idCliente, Pageable pageable);

    /** Devoluções de pedidos com itens do vendedor (ou de todos, quando idVendedor é nulo). */
    @Query("""
            SELECT d FROM Devolucao d
            WHERE (:idVendedor IS NULL OR EXISTS (
                      SELECT 1 FROM ItemPedido i WHERE i.pedido = d.pedido AND i.produto.vendedor.id = :idVendedor))
            AND   (:status IS NULL OR d.status = :status)
            """)
    Page<Devolucao> buscarRecebidas(Long idVendedor, StatusDevolucao status, Pageable pageable);
}
