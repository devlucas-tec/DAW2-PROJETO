package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.EnderecoDAO;
import br.edu.ifpb.es.daw.entities.Endereco;

public class EnderecoDAOImpl extends AbstractDAOImpl<Endereco> implements EnderecoDAO {

    public EnderecoDAOImpl() {

        super(Endereco.class);
    }
}