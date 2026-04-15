package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.EntregaDAO;
import br.edu.ifpb.es.daw.entities.Entrega;

public class EntregaDAOImpl extends AbstractDAOImpl<Entrega> implements EntregaDAO {

    public EntregaDAOImpl() {
        super(Entrega.class);
    }
}