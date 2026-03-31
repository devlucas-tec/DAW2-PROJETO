package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.CategoriaDAO;
import br.edu.ifpb.es.daw.dao.impl.CategoriaDAOImpl;
import br.edu.ifpb.es.daw.entities.Categoria;
import br.edu.ifpb.es.daw.util.JPAUtil;

import jakarta.persistence.EntityManager;

public class MainCategoriaSave {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        CategoriaDAO dao = new CategoriaDAOImpl(em);

        Categoria categoria = new Categoria();

        categoria.setNome("Categoria_" + System.nanoTime());

        categoria.setDescricao("Descrição de teste");

        dao.save(categoria);

        em.close();

        System.out.println("Categoria salva com sucesso!");
    }
}