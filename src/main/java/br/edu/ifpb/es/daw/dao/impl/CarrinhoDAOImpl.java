package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.entities.Carrinho;
import jakarta.persistence.EntityManager;

public class CarrinhoDAOImpl extends  AbstractDAOImpl<Carrinho> implements CarrinhoDAO {

    public CarrinhoDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

}
