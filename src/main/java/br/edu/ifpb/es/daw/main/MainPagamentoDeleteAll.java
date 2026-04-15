package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.PagamentoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.PagamentoDAOImpl;

public class MainPagamentoDeleteAll {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            PagamentoDAO dao = new PagamentoDAOImpl();

            dao.deleteAll();

            System.out.println("Todos os pagamentos foram removidos!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}