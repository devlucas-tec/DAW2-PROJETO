package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.ItemPedido;
import br.edu.ifpb.es.daw.model.ItemPedidoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, ItemPedidoId> {
}
