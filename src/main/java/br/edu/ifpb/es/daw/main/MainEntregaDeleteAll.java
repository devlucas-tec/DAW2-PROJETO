package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.EntregaDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.EntregaDAOImpl;

public class MainEntregaDeleteAll {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            EntregaDAO dao = new EntregaDAOImpl();

            dao.deleteAll();

            System.out.println("Todas as entregas foram removidas!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}