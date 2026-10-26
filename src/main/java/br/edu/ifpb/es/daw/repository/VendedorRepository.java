package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {

    Optional<Vendedor> findByCnpjCpf(String cnpjCpf);
}
