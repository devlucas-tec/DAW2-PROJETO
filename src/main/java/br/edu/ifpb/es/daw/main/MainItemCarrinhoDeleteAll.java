package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.impl.*;
import br.edu.ifpb.es.daw.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class MainItemCarrinhoDeleteAll {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        ItemCarrinhoDAO dao = new ItemCarrinhoDAOImpl(em);

        dao.deleteAll();

        em.close();

        System.out.println("Todos os itens do carrinho foram apagados!");

    }
}
