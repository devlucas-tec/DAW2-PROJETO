package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.PedidoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.PedidoDAOImpl;

public class MainPedidoDeleteAll {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            PedidoDAO dao = new PedidoDAOImpl();

            dao.deleteAll();

            System.out.println("Todos os pedidos foram removidos!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}