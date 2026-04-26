package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.ItemCarrinhoDAO;
import br.edu.ifpb.es.daw.entities.ItemCarrinho;
import br.edu.ifpb.es.daw.entities.ItemCarrinhoId;
import jakarta.persistence.EntityManager;

public class ItemCarrinhoDAOImpl extends AbstractDAOImpl<ItemCarrinho> implements ItemCarrinhoDAO {

    public ItemCarrinhoDAOImpl() {
        super(ItemCarrinho.class);
    }

    @Override
    public ItemCarrinho findByCarrinhoAndProduto(Long carrinhoId, Long produtoId) {
        try (EntityManager em = getEntityManager()) {
            ItemCarrinhoId id = new ItemCarrinhoId(carrinhoId, produtoId);
            return em.find(ItemCarrinho.class, id);
        }
    }
}