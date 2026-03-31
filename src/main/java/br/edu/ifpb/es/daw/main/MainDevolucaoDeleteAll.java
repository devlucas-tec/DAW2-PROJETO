package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.DevolucaoDAO;
import br.edu.ifpb.es.daw.dao.impl.DevolucaoDAOImpl;

import br.edu.ifpb.es.daw.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class MainDevolucaoDeleteAll {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        DevolucaoDAO dao = new DevolucaoDAOImpl(em);

        dao.deleteAll();

        em.close();

        System.out.println("Todas as devoluções foram removidas!");
    }
}