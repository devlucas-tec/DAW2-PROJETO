package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.ItemPedidoDAO;
import br.edu.ifpb.es.daw.entities.ItemPedido;

public class ItemPedidoDAOImpl extends AbstractDAOImpl<ItemPedido> implements ItemPedidoDAO {

    public ItemPedidoDAOImpl() {
        super(ItemPedido.class);
    }
}
