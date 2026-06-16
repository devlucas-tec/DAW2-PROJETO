package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("""
            SELECT p FROM Produto p
            WHERE (:nome        IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', CAST(:nome AS string), '%')))
            AND   (:idCategoria IS NULL OR p.categoria.id = :idCategoria)
            AND   (:precoMin    IS NULL OR p.preco >= :precoMin)
            AND   (:precoMax    IS NULL OR p.preco <= :precoMax)
            """)
    Page<Produto> filtrar(
            String nome,
            Long idCategoria,
            BigDecimal precoMin,
            BigDecimal precoMax,
            Pageable pageable
    );
}