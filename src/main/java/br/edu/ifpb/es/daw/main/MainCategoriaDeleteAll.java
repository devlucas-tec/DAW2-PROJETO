package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.CategoriaDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.CategoriaDAOImpl;

public class MainCategoriaDeleteAll {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            CategoriaDAO dao = new CategoriaDAOImpl();

            dao.deleteAll();

            System.out.println("Todas as categorias foram removidas!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }


    }
}