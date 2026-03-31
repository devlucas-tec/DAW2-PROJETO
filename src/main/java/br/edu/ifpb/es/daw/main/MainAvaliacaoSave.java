package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.AvaliacaoDAO;
import br.edu.ifpb.es.daw.dao.impl.AvaliacaoDAOImpl;
import br.edu.ifpb.es.daw.entities.Avaliacao;
import br.edu.ifpb.es.daw.util.JPAUtil;

import jakarta.persistence.EntityManager;

public class MainAvaliacaoSave {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        AvaliacaoDAO dao = new AvaliacaoDAOImpl(em);

        Avaliacao avaliacao = new Avaliacao();

        avaliacao.setNota(5);
        avaliacao.setComentario("Muito bom!");

        dao.save(avaliacao);

        em.close();

        System.out.println("Avaliação salva com sucesso!");
    }
}