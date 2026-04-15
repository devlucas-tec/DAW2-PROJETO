package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.ItemCarrinhoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ItemCarrinhoDAOImpl;
import br.edu.ifpb.es.daw.entities.ItemCarrinho;

import java.math.BigDecimal;

public class MainItemCarrinhoSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            ItemCarrinhoDAO dao = new ItemCarrinhoDAOImpl();

            ItemCarrinho itemCarrinho = new ItemCarrinho();

            itemCarrinho.setPrecoUnitario(BigDecimal.valueOf(100.25));

            itemCarrinho.setQuantidade(50);

            dao.save(itemCarrinho);

            System.out.println("Item do carrinho salvo");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}
