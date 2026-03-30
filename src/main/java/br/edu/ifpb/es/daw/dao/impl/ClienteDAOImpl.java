package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.ClienteDAO;
import br.edu.ifpb.es.daw.entities.Cliente;
import jakarta.persistence.EntityManager;

public class ClienteDAOImpl extends AbstractDAOImpl<Cliente> implements ClienteDAO {

    public ClienteDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }
}