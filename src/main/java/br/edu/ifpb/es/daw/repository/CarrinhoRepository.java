package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Carrinho;
import br.edu.ifpb.es.daw.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {

    boolean existsByCliente(Cliente cliente);

    Optional<Carrinho> findByClienteId(Long idCliente);
}
