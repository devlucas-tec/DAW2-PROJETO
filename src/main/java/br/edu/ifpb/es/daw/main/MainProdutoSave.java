package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.ProdutoDAO;
import br.edu.ifpb.es.daw.dao.impl.ProdutoDAOImpl;
import br.edu.ifpb.es.daw.entities.Produto;

import br.edu.ifpb.es.daw.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;

public class MainProdutoSave {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        ProdutoDAO dao = new ProdutoDAOImpl(em);

        Produto produto = new Produto();

        produto.setNome("Notebook");
        produto.setDescricao("Notebook Dell i7");
        produto.setEstoque(10);
        produto.setPreco(new BigDecimal("3500.00"));

        dao.save(produto);

        em.close();

        System.out.println("Produto salvo com sucesso!");
    }
}