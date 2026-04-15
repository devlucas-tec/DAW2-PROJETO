package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.CarrinhoDAO;
import br.edu.ifpb.es.daw.entities.Carrinho;


public class CarrinhoDAOImpl extends  AbstractDAOImpl<Carrinho> implements CarrinhoDAO {

    public CarrinhoDAOImpl() {
        super(Carrinho.class);
    }

}
