package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.DevolucaoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.DevolucaoDAOImpl;
import br.edu.ifpb.es.daw.entities.Devolucao;
import br.edu.ifpb.es.daw.entities.StatusDevolucao;

public class MainDevolucaoSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            DevolucaoDAO dao = new DevolucaoDAOImpl();

            Devolucao devolucao = new Devolucao();

            devolucao.setMotivo("Produto com defeito");
            devolucao.setStatus(StatusDevolucao.APROVADA);

            dao.save(devolucao);

            System.out.println("Devolução salva com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}