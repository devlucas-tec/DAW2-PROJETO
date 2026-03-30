package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.EntregaDAO;
import br.edu.ifpb.es.daw.entities.Entrega;
import jakarta.persistence.EntityManager;

public class EntregaDAOImpl extends AbstractDAOImpl<Entrega> implements EntregaDAO {

    public EntregaDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }
}