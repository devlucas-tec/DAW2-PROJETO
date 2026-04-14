package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.AvaliacaoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.AvaliacaoDAOImpl;
import br.edu.ifpb.es.daw.entities.Avaliacao;
import br.edu.ifpb.es.daw.util.JPAUtil;

import jakarta.persistence.EntityManager;

public class MainAvaliacaoSave {

    public static void main(String[] args) {

        try{

            AbstractDAOImpl.initialize("daw");

            AvaliacaoDAO dao = new AvaliacaoDAOImpl();

            Avaliacao avaliacao = new Avaliacao();

            avaliacao.setNota(5);
            avaliacao.setComentario("Muito bom!");

            dao.save(avaliacao);

            System.out.println("Avaliação salva com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }

    }
}