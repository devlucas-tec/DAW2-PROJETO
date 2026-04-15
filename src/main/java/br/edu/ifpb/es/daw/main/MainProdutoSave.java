package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.ProdutoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ProdutoDAOImpl;
import br.edu.ifpb.es.daw.entities.Produto;

import java.math.BigDecimal;

public class MainProdutoSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            ProdutoDAO dao = new ProdutoDAOImpl();

            Produto produto = new Produto();

            produto.setNome("Notebook");
            produto.setDescricao("Notebook Dell i7");
            produto.setEstoque(10);
            produto.setPreco(new BigDecimal("3500.00"));

            dao.save(produto);

            System.out.println("Produto salvo com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}