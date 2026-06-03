package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.CategoriaDAO;
import br.edu.ifpb.es.daw.model.Categoria;

public class CategoriaDAOImpl extends AbstractDAOImpl<Categoria> implements CategoriaDAO {

    public CategoriaDAOImpl() {

        super(Categoria.class);
    }
}