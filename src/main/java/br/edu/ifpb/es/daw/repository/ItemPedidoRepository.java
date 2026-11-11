package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.ItemPedido;
import br.edu.ifpb.es.daw.model.ItemPedidoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, ItemPedidoId> {

    @Query("""
            SELECT i FROM ItemPedido i
            JOIN FETCH i.pedido p
            JOIN FETCH i.produto pr
            WHERE pr.vendedor.id = :idVendedor
            AND p.dataPedido >= :inicio
            """)
    List<ItemPedido> buscarDoVendedorDesde(Long idVendedor, LocalDateTime inicio);

    @Query("""
            SELECT i FROM ItemPedido i
            JOIN FETCH i.pedido p
            JOIN FETCH i.produto pr
            JOIN FETCH pr.vendedor
            JOIN FETCH pr.categoria
            WHERE p.dataPedido >= :inicio
            """)
    List<ItemPedido> buscarDesde(LocalDateTime inicio);
}
