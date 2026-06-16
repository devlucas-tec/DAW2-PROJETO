package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Cupom;
import br.edu.ifpb.es.daw.model.enums.StatusCupom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CupomRepository extends JpaRepository<Cupom, Long> {

    @Query("SELECT c FROM Cupom c WHERE c.codigo = :codigo")
    Optional<Cupom> buscarPorCodigo(String codigo);

    @Query("""
            SELECT c FROM Cupom c
            WHERE (:codigo IS NULL OR LOWER(c.codigo) LIKE LOWER(CONCAT('%', CAST(:codigo AS string), '%')))
            AND   (:status IS NULL OR c.status = :status)
            """)
    Page<Cupom> filtrar(String codigo, StatusCupom status, Pageable pageable);
}