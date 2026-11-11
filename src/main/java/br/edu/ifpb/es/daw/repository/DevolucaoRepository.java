package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Devolucao;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.StatusDevolucao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface DevolucaoRepository extends JpaRepository<Devolucao, Long> {

    boolean existsByPedidoAndStatusIn(Pedido pedido, Collection<StatusDevolucao> status);

    Page<Devolucao> findByPedidoClienteId(Long idCliente, Pageable pageable);
}
