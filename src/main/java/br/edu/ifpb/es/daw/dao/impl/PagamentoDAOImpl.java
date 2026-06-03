package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.PagamentoDAO;
import br.edu.ifpb.es.daw.model.Pagamento;

public class PagamentoDAOImpl extends AbstractDAOImpl<Pagamento> implements PagamentoDAO {

    public PagamentoDAOImpl() {
        super(Pagamento.class);
    }
}