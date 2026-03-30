package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.EnderecoDAO;
import br.edu.ifpb.es.daw.entities.Endereco;
import jakarta.persistence.EntityManager;

public class EnderecoDAOImpl extends AbstractDAOImpl<Endereco> implements EnderecoDAO {

    public EnderecoDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }
}