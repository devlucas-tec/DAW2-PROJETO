package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.ClienteDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ClienteDAOImpl;

public class MainClienteDeleteAll {

    public static void main(String[] args) {

        try{

            AbstractDAOImpl.initialize("daw");

            ClienteDAO dao = new ClienteDAOImpl();

            dao.deleteAll();

            System.out.println("Todos os clientes foram removidos!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }


    }
}