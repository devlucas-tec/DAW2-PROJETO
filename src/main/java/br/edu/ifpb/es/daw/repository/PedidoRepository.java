package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :idCliente")
    Page<Pedido> buscarPorCliente(Long idCliente, Pageable pageable);

    @Query(value = """
            SELECT DISTINCT p FROM Pedido p JOIN p.itens i
            WHERE i.produto.vendedor.id = :idVendedor
            """,
            countQuery = """
            SELECT COUNT(DISTINCT p) FROM Pedido p JOIN p.itens i
            WHERE i.produto.vendedor.id = :idVendedor
            """)
    Page<Pedido> buscarPorVendedor(Long idVendedor, Pageable pageable);

    @Query("""
            SELECT COUNT(i) > 0 FROM ItemPedido i
            WHERE i.pedido.cliente.id = :idCliente
            AND i.produto.id = :idProduto
            AND i.pedido.status = br.edu.ifpb.es.daw.model.enums.StatusPedido.CONFIRMADO
            """)
    boolean clienteComprouProduto(Long idCliente, Long idProduto);
}
