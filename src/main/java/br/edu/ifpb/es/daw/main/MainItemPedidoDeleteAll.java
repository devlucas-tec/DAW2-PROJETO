package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.ItemPedidoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ItemPedidoDAOImpl;

public class MainItemPedidoDeleteAll {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            ItemPedidoDAO dao = new ItemPedidoDAOImpl();

            dao.deleteAll();

            System.out.println("Todos os itens de pedido foram apagados!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}
