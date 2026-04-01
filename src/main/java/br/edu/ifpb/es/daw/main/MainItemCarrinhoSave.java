package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.impl.ItemCarrinhoDAO;
import br.edu.ifpb.es.daw.dao.impl.ItemCarrinhoDAOImpl;
import br.edu.ifpb.es.daw.entities.ItemCarrinho;
import br.edu.ifpb.es.daw.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class MainItemCarrinhoSave {

    public static void main(String[] args) {
        EntityManager em = JPAUtil.getEntityManager();

        ItemCarrinhoDAO dao = new ItemCarrinhoDAOImpl(em);

        ItemCarrinho itemCarrinho = new ItemCarrinho();

        itemCarrinho.setQuantidade(50);

        dao.save(itemCarrinho);

        em.close();

        System.out.println("Item do carrinho salvo");
    }
}
