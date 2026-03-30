package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.VendedorDAO;
import br.edu.ifpb.es.daw.entities.Vendedor;
import jakarta.persistence.EntityManager;

public class VendedorDAOImpl extends AbstractDAOImpl<Vendedor> implements VendedorDAO {

    public VendedorDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }
}