package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.ItemPedido;
import br.edu.ifpb.es.daw.model.ItemPedidoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import org.springframework.data.domain.Pageable;
import java.util.Collection;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, ItemPedidoId> {

    boolean existsByProdutoId(Long idProduto);

    boolean existsByProdutoVendedorId(Long idVendedor);

    /** Ids dos produtos mais vendidos (soma das quantidades), só entre os que estão à venda. */
    @Query("""
            SELECT i.produto.id FROM ItemPedido i
            WHERE i.pedido.status NOT IN :statusIgnorados
            AND   i.produto.ativo = true
            AND   i.produto.estoque > 0
            AND   i.produto.vendedor.ativo = true
            AND   i.produto.vendedor.lojaAberta = true
            AND   (:idCategoria IS NULL OR i.produto.categoria.id = :idCategoria)
            GROUP BY i.produto.id
            ORDER BY SUM(i.quantidade) DESC, i.produto.id ASC
            """)
    List<Long> idsMaisVendidos(Collection<StatusPedido> statusIgnorados, Long idCategoria, Pageable pageable);

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
