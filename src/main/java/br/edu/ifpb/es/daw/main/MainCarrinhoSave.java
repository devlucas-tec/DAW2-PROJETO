package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.CarrinhoDAO;
import br.edu.ifpb.es.daw.dao.impl.CarrinhoDAOImpl;
import br.edu.ifpb.es.daw.entities.Carrinho;
import br.edu.ifpb.es.daw.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class MainCarrinhoSave {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        CarrinhoDAO dao = new CarrinhoDAOImpl(em);

        Carrinho carrinho = new Carrinho();

        dao.save(carrinho);

        em.close();

        System.out.println("Carrinho salvo com sucesso");

    }
}
