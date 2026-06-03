package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Categoria;
import br.edu.ifpb.es.daw.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p FROM Produto p WHERE p.categoria = :categoria")
    List<Produto> buscarPorCategoria(Categoria categoria);

    @Query("SELECT p FROM Produto p WHERE (:nome is null or p.nome LIKE %:nome%)")
    List<Produto> buscarPorNome(String nome);
}
