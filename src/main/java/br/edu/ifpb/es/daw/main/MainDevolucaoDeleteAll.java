package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.DevolucaoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.DevolucaoDAOImpl;

public class MainDevolucaoDeleteAll {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            DevolucaoDAO dao = new DevolucaoDAOImpl();

            dao.deleteAll();

            System.out.println("Todas as devoluções foram removidas!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}