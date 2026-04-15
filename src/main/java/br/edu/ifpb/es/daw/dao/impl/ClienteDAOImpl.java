package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.ClienteDAO;
import br.edu.ifpb.es.daw.entities.Cliente;


public class ClienteDAOImpl extends AbstractDAOImpl<Cliente> implements ClienteDAO {

    public ClienteDAOImpl() {

        super(Cliente.class);
    }
}