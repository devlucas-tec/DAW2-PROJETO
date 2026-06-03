package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Carrinho;
import br.edu.ifpb.es.daw.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {

    @Query("SELECT c FROM Carrinho c WHERE c.cliente = :cliente")
    Optional<Carrinho> buscarPorCliente(Cliente cliente);
}
