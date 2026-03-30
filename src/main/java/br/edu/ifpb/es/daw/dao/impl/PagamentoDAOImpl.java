package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.PagamentoDAO;
import br.edu.ifpb.es.daw.entities.Pagamento;
import jakarta.persistence.EntityManager;

public class PagamentoDAOImpl extends AbstractDAOImpl<Pagamento> implements PagamentoDAO {

    public PagamentoDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }
}