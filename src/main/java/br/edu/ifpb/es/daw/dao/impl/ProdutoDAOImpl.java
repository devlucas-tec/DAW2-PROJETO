package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.ProdutoDAO;
import br.edu.ifpb.es.daw.entities.Produto;
import jakarta.persistence.EntityManager;

public class ProdutoDAOImpl extends AbstractDAOImpl<Produto> implements ProdutoDAO {

    public ProdutoDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }
}