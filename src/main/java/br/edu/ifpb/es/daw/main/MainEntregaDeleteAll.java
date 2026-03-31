package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.EntregaDAO;
import br.edu.ifpb.es.daw.dao.impl.EntregaDAOImpl;
import br.edu.ifpb.es.daw.util.JPAUtil;

import jakarta.persistence.EntityManager;

public class MainEntregaDeleteAll {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        EntregaDAO dao = new EntregaDAOImpl(em);

        dao.deleteAll();

        em.close();

        System.out.println("Todas as entregas foram removidas!");
    }
}