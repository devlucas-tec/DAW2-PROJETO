package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.CarrinhoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.CarrinhoDAOImpl;


public class MainCarrinhoDeleteAll {

    public static void main(String[] args) {

        try {
            AbstractDAOImpl.initialize("daw");

            CarrinhoDAO dao = new CarrinhoDAOImpl();

            dao.deleteAll();

            System.out.println("Todos os carrinhos foram apagados!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}
