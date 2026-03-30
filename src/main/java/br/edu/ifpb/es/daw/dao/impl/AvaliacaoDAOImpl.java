package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.AvaliacaoDAO;
import br.edu.ifpb.es.daw.entities.Avaliacao;
import jakarta.persistence.EntityManager;

public class AvaliacaoDAOImpl extends AbstractDAOImpl<Avaliacao> implements AvaliacaoDAO {

    public AvaliacaoDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }
}