package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.PedidoDAO;
import br.edu.ifpb.es.daw.dao.impl.PedidoDAOImpl;
import br.edu.ifpb.es.daw.util.JPAUtil;

import jakarta.persistence.EntityManager;

public class MainPedidoDeleteAll {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        PedidoDAO dao = new PedidoDAOImpl(em);

        dao.deleteAll();

        em.close();

        System.out.println("Todos os pedidos foram removidos!");
    }
}