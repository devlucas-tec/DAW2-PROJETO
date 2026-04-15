package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.CarrinhoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.CarrinhoDAOImpl;
import br.edu.ifpb.es.daw.entities.Carrinho;


public class MainCarrinhoSave {

    public static void main(String[] args) {

        try{

            AbstractDAOImpl.initialize("daw");

            CarrinhoDAO dao = new CarrinhoDAOImpl();

            Carrinho carrinho = new Carrinho();

            dao.save(carrinho);

            System.out.println("Carrinho salvo com sucesso");

        } finally {

            AbstractDAOImpl.closeFactory();

        }


    }
}
