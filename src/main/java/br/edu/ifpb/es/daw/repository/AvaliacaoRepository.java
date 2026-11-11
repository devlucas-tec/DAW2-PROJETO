package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Avaliacao;
import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    boolean existsByClienteAndProduto(Cliente cliente, Produto produto);

    Page<Avaliacao> findByProdutoId(Long idProduto, Pageable pageable);

    Page<Avaliacao> findByClienteId(Long idCliente, Pageable pageable);

    long countByProdutoId(Long idProduto);

    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.produto.id = :idProduto")
    Double mediaPorProduto(Long idProduto);

    long countByProdutoVendedorId(Long idVendedor);

    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.produto.vendedor.id = :idVendedor")
    Double mediaPorVendedor(Long idVendedor);
}
