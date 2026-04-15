package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.EnderecoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.EnderecoDAOImpl;


public class MainEnderecoDeleteAll {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            EnderecoDAO dao = new EnderecoDAOImpl();

            dao.deleteAll();

            System.out.println("Todos os endereços foram removidos!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}