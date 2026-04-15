package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.ItemCarrinhoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ItemCarrinhoDAOImpl;

public class MainItemCarrinhoDeleteAll {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            ItemCarrinhoDAO dao = new ItemCarrinhoDAOImpl();

            dao.deleteAll();

            System.out.println("Todos os itens do carrinho foram apagados!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}
