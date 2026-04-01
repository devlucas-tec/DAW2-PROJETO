package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.impl.ItemPedidoDAO;
import br.edu.ifpb.es.daw.dao.impl.ItemPedidoDAOImpl;
import br.edu.ifpb.es.daw.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class MainItemPedidoDeleteAll {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        ItemPedidoDAO dao = new ItemPedidoDAOImpl(em);

        dao.deleteAll();

        em.close();


        System.out.println("Todos os carrinhos foram apagados!");
    }
}
