package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.ProdutoDAO;
import br.edu.ifpb.es.daw.entities.Produto;

public class ProdutoDAOImpl extends AbstractDAOImpl<Produto> implements ProdutoDAO {

    public ProdutoDAOImpl() {
        super(Produto.class);
    }
}