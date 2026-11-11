package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Compra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    Optional<Compra> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    Page<Compra> findByClienteId(Long idCliente, Pageable pageable);
}
