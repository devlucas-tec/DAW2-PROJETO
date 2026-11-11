package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Entrega;
import br.edu.ifpb.es.daw.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    Optional<Entrega> findByPedido(Pedido pedido);

    boolean existsByCodigoRastreamento(String codigoRastreamento);

    Optional<Entrega> findByCodigoRastreamento(String codigoRastreamento);
}
