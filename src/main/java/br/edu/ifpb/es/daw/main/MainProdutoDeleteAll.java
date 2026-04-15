package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.ProdutoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ProdutoDAOImpl;

public class MainProdutoDeleteAll {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            ProdutoDAO dao = new ProdutoDAOImpl();

            dao.deleteAll();

            System.out.println("Todos os produtos foram removidos!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}