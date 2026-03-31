package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.AvaliacaoDAO;
import br.edu.ifpb.es.daw.dao.impl.AvaliacaoDAOImpl;
import br.edu.ifpb.es.daw.util.JPAUtil;

import jakarta.persistence.EntityManager;

public class MainAvaliacaoDeleteAll {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        AvaliacaoDAO dao = new AvaliacaoDAOImpl(em);

        dao.deleteAll();

        em.close();

        System.out.println("Todas as avaliações foram removidas!");
    }
}