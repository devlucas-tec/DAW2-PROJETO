package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.PedidoDAO;
import br.edu.ifpb.es.daw.entities.Pedido;

public class PedidoDAOImpl extends AbstractDAOImpl<Pedido> implements PedidoDAO {

    public PedidoDAOImpl() {
        super(Pedido.class);
    }
}