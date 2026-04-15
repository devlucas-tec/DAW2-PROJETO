package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.DevolucaoDAO;
import br.edu.ifpb.es.daw.entities.Devolucao;

public class DevolucaoDAOImpl extends AbstractDAOImpl<Devolucao> implements DevolucaoDAO {

    public DevolucaoDAOImpl() {

        super(Devolucao.class);
    }
}