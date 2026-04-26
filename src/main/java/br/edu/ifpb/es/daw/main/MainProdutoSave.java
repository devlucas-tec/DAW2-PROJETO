package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.CategoriaDAO;
import br.edu.ifpb.es.daw.dao.ProdutoDAO;
import br.edu.ifpb.es.daw.dao.VendedorDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.CategoriaDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ProdutoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.VendedorDAOImpl;
import br.edu.ifpb.es.daw.entities.Categoria;
import br.edu.ifpb.es.daw.entities.Produto;
import br.edu.ifpb.es.daw.entities.Vendedor;

import java.math.BigDecimal;
import java.util.List;

public class MainProdutoSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            ProdutoDAO produtoDAO = new ProdutoDAOImpl();
            CategoriaDAO categoriaDAO = new CategoriaDAOImpl();
            VendedorDAO vendedorDAO = new VendedorDAOImpl();

            List<Categoria> categorias = categoriaDAO.findAll();
            List<Vendedor> vendedores = vendedorDAO.findAll();

            System.out.println("Categorias no banco: " + categorias.size());
            System.out.println("Vendedores no banco: " + vendedores.size());

            Categoria categoria = categorias.isEmpty() ? null : categorias.get(0);
            Vendedor vendedor = vendedores.isEmpty() ? null : vendedores.get(0);

            System.out.println("ID da Categoria encontrada: " + (categoria != null ? categoria.getId() : "NULL"));
            System.out.println("ID do Vendedor encontrado: " + (vendedor != null ? vendedor.getId() : "NULL"));

            if (categoria == null || vendedor == null) {
                System.out.println("❌ Não há categorias ou vendedores no banco!");
                return;
            }

            Produto produto = new Produto();
            produto.setNome("Notebook");
            produto.setDescricao("Notebook Dell i7");
            produto.setEstoque(10);
            produto.setPreco(new BigDecimal("3500.00"));
            produto.setCategoria(categoria);
            produto.setVendedor(vendedor);

            produtoDAO.save(produto);

            System.out.println("Produto salvo com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}