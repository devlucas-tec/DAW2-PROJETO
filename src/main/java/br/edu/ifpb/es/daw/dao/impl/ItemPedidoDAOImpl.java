package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.entities.ItemPedido;
import jakarta.persistence.EntityManager;

public class ItemPedidoDAOImpl extends AbstractDAOImpl<ItemPedido> implements ItemPedidoDAO {

    public ItemPedidoDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }
}
