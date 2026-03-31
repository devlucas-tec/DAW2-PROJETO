package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.EnderecoDAO;
import br.edu.ifpb.es.daw.dao.impl.EnderecoDAOImpl;

import br.edu.ifpb.es.daw.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class MainEnderecoDeleteAll {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        EnderecoDAO dao = new EnderecoDAOImpl(em);

        dao.deleteAll();

        em.close();

        System.out.println("Todos os endereços foram removidos!");
    }
}