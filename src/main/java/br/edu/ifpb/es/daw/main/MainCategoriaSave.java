package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.CategoriaDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.CategoriaDAOImpl;
import br.edu.ifpb.es.daw.entities.Categoria;

public class MainCategoriaSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            CategoriaDAO dao = new CategoriaDAOImpl();

            Categoria categoria = new Categoria();

            categoria.setNome("Categoria_" + System.nanoTime());

            categoria.setDescricao("Descrição de teste");

            dao.save(categoria);

            System.out.println("Categoria salva com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }


    }
}