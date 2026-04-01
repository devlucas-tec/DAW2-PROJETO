package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.DevolucaoDAO;
import br.edu.ifpb.es.daw.dao.impl.DevolucaoDAOImpl;
import br.edu.ifpb.es.daw.entities.Devolucao;

import br.edu.ifpb.es.daw.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;

public class MainDevolucaoSave {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        DevolucaoDAO dao = new DevolucaoDAOImpl(em);

        Devolucao devolucao = new Devolucao();

        devolucao.setMotivo("Produto com defeito");
        devolucao.setStatus("ATIVO");

        dao.save(devolucao);

        em.close();

        System.out.println("Devolução salva com sucesso!");
    }
}