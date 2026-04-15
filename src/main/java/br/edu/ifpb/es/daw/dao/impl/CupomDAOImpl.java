package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.CupomDAO;
import br.edu.ifpb.es.daw.entities.Cupom;


public class CupomDAOImpl extends AbstractDAOImpl<Cupom> implements CupomDAO {

    public CupomDAOImpl() {

        super(Cupom.class);
    }
}