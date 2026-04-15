package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.CupomDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.CupomDAOImpl;

public class MainCupomDeleteAll {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            CupomDAO dao = new CupomDAOImpl();

            dao.deleteAll();

            System.out.println("Todos os cupons foram removidos!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}