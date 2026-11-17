package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Favorito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    Optional<Favorito> findByClienteIdAndProdutoId(Long idCliente, Long idProduto);

    Page<Favorito> findByClienteId(Long idCliente, Pageable pageable);

    @Query("SELECT f.produto.id FROM Favorito f WHERE f.cliente.id = :idCliente ORDER BY f.dataCriacao DESC")
    List<Long> idsDosProdutos(Long idCliente);

    @Modifying
    @Query("DELETE FROM Favorito f WHERE f.produto.id IN :idsProdutos")
    int removerDosProdutos(Collection<Long> idsProdutos);

    @Modifying
    @Query("DELETE FROM Favorito f WHERE f.cliente.id = :idCliente")
    int removerDoCliente(Long idCliente);
}
