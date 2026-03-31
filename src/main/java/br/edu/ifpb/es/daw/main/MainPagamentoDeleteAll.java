package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.PagamentoDAO;
import br.edu.ifpb.es.daw.dao.impl.PagamentoDAOImpl;

import br.edu.ifpb.es.daw.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class MainPagamentoDeleteAll {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        PagamentoDAO dao = new PagamentoDAOImpl(em);

        dao.deleteAll();

        em.close();

        System.out.println("Todos os pagamentos foram removidos!");
    }
}