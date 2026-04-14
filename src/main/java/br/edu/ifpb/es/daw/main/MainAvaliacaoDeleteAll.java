package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.AvaliacaoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.AvaliacaoDAOImpl;

public class MainAvaliacaoDeleteAll {

    public static void main(String[] args) {

        try{

            AbstractDAOImpl.initialize("daw");

            AvaliacaoDAO dao = new AvaliacaoDAOImpl();

            dao.deleteAll();

            System.out.println("Todas as avaliações foram removidas!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }

    }
}