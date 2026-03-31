package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.ProdutoDAO;
import br.edu.ifpb.es.daw.dao.impl.ProdutoDAOImpl;

import br.edu.ifpb.es.daw.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class MainProdutoDeleteAll {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        ProdutoDAO dao = new ProdutoDAOImpl(em);

        dao.deleteAll();

        em.close();

        System.out.println("Todos os produtos foram removidos!");
    }
}