package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Produto;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    /** Vitrine pública: só anúncios ativos de vendedores ativos. */
    @Query("""
            SELECT p FROM Produto p
            WHERE p.ativo = true
            AND   p.vendedor.ativo = true
            AND   (:somenteComEstoque = false OR p.estoque > 0)
            AND   (:nome        IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', CAST(:nome AS string), '%')))
            AND   (:idCategoria IS NULL OR p.categoria.id = :idCategoria)
            AND   (:idVendedor  IS NULL OR p.vendedor.id = :idVendedor)
            AND   (:precoMin    IS NULL OR p.preco >= :precoMin)
            AND   (:precoMax    IS NULL OR p.preco <= :precoMax)
            """)
    Page<Produto> buscarVitrine(
            String nome,
            Long idCategoria,
            Long idVendedor,
            BigDecimal precoMin,
            BigDecimal precoMax,
            boolean somenteComEstoque,
            Pageable pageable
    );

    /** Painel do vendedor: todos os produtos dele, inclusive inativos e sem estoque. */
    @Query("""
            SELECT p FROM Produto p
            WHERE p.vendedor.id = :idVendedor
            AND   (:nome  IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', CAST(:nome AS string), '%')))
            AND   (:ativo IS NULL OR p.ativo = :ativo)
            """)
    Page<Produto> buscarDoVendedor(Long idVendedor, String nome, Boolean ativo, Pageable pageable);

    List<Produto> findByVendedorIdAndEstoqueLessThanEqualOrderByEstoqueAscNomeAsc(Long idVendedor, int estoque);

    long countByVendedorIdAndAtivoTrue(Long idVendedor);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Produto p WHERE p.id IN :ids")
    List<Produto> buscarParaAtualizacao(Collection<Long> ids);

    @Modifying
    @Query(value = "DELETE FROM produto_imagem", nativeQuery = true)
    void removerTodasAsImagens();
}
