package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Cupom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CupomRepository extends JpaRepository<Cupom, Long> {

    @Query("SELECT c FROM Cupom c WHERE c.codigo = :codigo")
    Optional<Cupom> buscarPorCodigo(String codigo);
}
