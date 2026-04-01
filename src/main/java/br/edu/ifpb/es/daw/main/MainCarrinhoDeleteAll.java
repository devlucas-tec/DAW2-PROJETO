package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.impl.CarrinhoDAO;
import br.edu.ifpb.es.daw.dao.impl.CarrinhoDAOImpl;
import br.edu.ifpb.es.daw.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class MainCarrinhoDeleteAll {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        CarrinhoDAO dao = new CarrinhoDAOImpl(em);

        dao.deleteAll();

        em.close();

        System.out.println("Todos os carrinhos foram apagados!");
    }
}
