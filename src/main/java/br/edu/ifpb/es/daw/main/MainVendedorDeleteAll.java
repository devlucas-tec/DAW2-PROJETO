package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.VendedorDAO;
import br.edu.ifpb.es.daw.dao.impl.VendedorDAOImpl;

import br.edu.ifpb.es.daw.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class MainVendedorDeleteAll {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        VendedorDAO dao = new VendedorDAOImpl(em);

        dao.deleteAll();

        em.close();

        System.out.println("Todos os vendedores foram removidos!");
    }
}