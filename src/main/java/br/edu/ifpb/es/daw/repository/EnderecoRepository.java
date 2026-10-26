package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Endereco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    @Query("SELECT e FROM Endereco e WHERE e.cliente.id = :idCliente")
    Page<Endereco> buscarPorCliente(Long idCliente, Pageable pageable);
}
