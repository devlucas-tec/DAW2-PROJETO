package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Cupom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CupomRepository extends JpaRepository<Cupom, Long> {
}
