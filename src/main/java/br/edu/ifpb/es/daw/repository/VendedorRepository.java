package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {

    Optional<Vendedor> findByCnpjCpf(String cnpjCpf);

    /** Lojas que aparecem para os clientes e têm pelo menos um produto à venda. */
    @Query("""
            SELECT v FROM Vendedor v
            WHERE v.ativo = true AND v.lojaAberta = true
            AND EXISTS (SELECT 1 FROM Produto p WHERE p.vendedor = v AND p.ativo = true AND p.estoque > 0)
            """)
    List<Vendedor> buscarLojasComProdutosAVenda();
}
