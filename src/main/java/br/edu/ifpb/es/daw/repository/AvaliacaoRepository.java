package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

}
