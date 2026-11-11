package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p WHERE (:status IS NULL OR p.status = :status)")
    Page<Pedido> buscarTodos(StatusPedido status, Pageable pageable);

    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :idCliente AND (:status IS NULL OR p.status = :status)")
    Page<Pedido> buscarPorCliente(Long idCliente, StatusPedido status, Pageable pageable);

    @Query(value = """
            SELECT DISTINCT p FROM Pedido p JOIN p.itens i
            WHERE i.produto.vendedor.id = :idVendedor
            AND (:status IS NULL OR p.status = :status)
            """,
            countQuery = """
            SELECT COUNT(DISTINCT p) FROM Pedido p JOIN p.itens i
            WHERE i.produto.vendedor.id = :idVendedor
            AND (:status IS NULL OR p.status = :status)
            """)
    Page<Pedido> buscarPorVendedor(Long idVendedor, StatusPedido status, Pageable pageable);

    /** O cliente só pode avaliar um produto que recebeu (entrega ENTREGUE). */
    @Query("""
            SELECT COUNT(i) > 0 FROM ItemPedido i JOIN i.pedido p JOIN p.entrega e
            WHERE p.cliente.id = :idCliente
            AND i.produto.id = :idProduto
            AND p.status <> br.edu.ifpb.es.daw.model.enums.StatusPedido.CANCELADO
            AND e.statusEntrega = br.edu.ifpb.es.daw.model.enums.StatusEntrega.ENTREGUE
            """)
    boolean clienteRecebeuProduto(Long idCliente, Long idProduto);

    List<Pedido> findByDataPedidoGreaterThanEqual(LocalDateTime inicio);
}
