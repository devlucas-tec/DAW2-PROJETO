package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.VendedorDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.VendedorDAOImpl;

public class MainVendedorDeleteAll {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            VendedorDAO dao = new VendedorDAOImpl();

            dao.deleteAll();

            System.out.println("Todos os vendedores foram removidos!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}