package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.entities.ItemCarrinho;
import jakarta.persistence.EntityManager;

public class ItemCarrinhoDAOImpl extends AbstractDAOImpl<ItemCarrinho> implements ItemCarrinhoDAO {

    public ItemCarrinhoDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }
}
