package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT c FROM Cliente c WHERE c.email = :email")
    Optional<Cliente> buscarPorEmail(String email);
}
