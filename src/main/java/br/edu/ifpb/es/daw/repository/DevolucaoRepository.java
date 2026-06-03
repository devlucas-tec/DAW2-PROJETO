package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Devolucao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevolucaoRepository extends JpaRepository<Devolucao, Long> {
}
