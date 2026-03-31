package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.ClienteDAO;
import br.edu.ifpb.es.daw.dao.impl.ClienteDAOImpl;
import br.edu.ifpb.es.daw.util.JPAUtil;

import jakarta.persistence.EntityManager;

public class MainClienteDeleteAll {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        ClienteDAO dao = new ClienteDAOImpl(em);

        dao.deleteAll();

        em.close();

        System.out.println("Todos os clientes foram removidos!");
    }
}