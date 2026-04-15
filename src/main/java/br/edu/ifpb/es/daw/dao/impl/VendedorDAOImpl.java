package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.VendedorDAO;
import br.edu.ifpb.es.daw.entities.Vendedor;

public class VendedorDAOImpl extends AbstractDAOImpl<Vendedor> implements VendedorDAO {

    public VendedorDAOImpl() {
        super(Vendedor.class);
    }
}