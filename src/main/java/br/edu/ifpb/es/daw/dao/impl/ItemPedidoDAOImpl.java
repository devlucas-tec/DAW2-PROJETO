package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.ItemPedidoDAO;
import br.edu.ifpb.es.daw.entities.ItemPedido;
import br.edu.ifpb.es.daw.entities.ItemPedidoId;
import jakarta.persistence.EntityManager;

public class ItemPedidoDAOImpl extends AbstractDAOImpl<ItemPedido> implements ItemPedidoDAO {

    public ItemPedidoDAOImpl() {
        super(ItemPedido.class);
    }

    @Override
    public ItemPedido findByPedidoAndProduto(Long pedidoId, Long produtoId) {
        try (EntityManager em = getEntityManager()) {
            ItemPedidoId id = new ItemPedidoId(pedidoId, produtoId);
            return em.find(ItemPedido.class, id);
        }
    }
}