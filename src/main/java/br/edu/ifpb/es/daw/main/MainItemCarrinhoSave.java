package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.CarrinhoDAO;
import br.edu.ifpb.es.daw.dao.ItemCarrinhoDAO;
import br.edu.ifpb.es.daw.dao.ProdutoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.CarrinhoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ItemCarrinhoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ProdutoDAOImpl;
import br.edu.ifpb.es.daw.entities.Carrinho;
import br.edu.ifpb.es.daw.entities.ItemCarrinho;
import br.edu.ifpb.es.daw.entities.ItemCarrinhoId;
import br.edu.ifpb.es.daw.entities.Produto;

import java.math.BigDecimal;
import java.util.List;

public class MainItemCarrinhoSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            ItemCarrinhoDAO itemCarrinhoDAO = new ItemCarrinhoDAOImpl();
            CarrinhoDAO carrinhoDAO = new CarrinhoDAOImpl();
            ProdutoDAO produtoDAO = new ProdutoDAOImpl();

            List<Carrinho> carrinhos = carrinhoDAO.findAll();
            List<Produto> produtos = produtoDAO.findAll();

            System.out.println("Carrinhos no banco: " + carrinhos.size());
            System.out.println("Produtos no banco: " + produtos.size());

            Carrinho carrinho = carrinhos.isEmpty() ? null : carrinhos.get(0);
            Produto produto = produtos.isEmpty() ? null : produtos.get(0);

            System.out.println("ID do Carrinho encontrado: " + (carrinho != null ? carrinho.getId() : "NULL"));
            System.out.println("ID do Produto encontrado: " + (produto != null ? produto.getId() : "NULL"));

            if (carrinho == null || produto == null) {
                System.out.println("❌ Não há carrinhos ou produtos no banco!");
                return;
            }

            // Monta a chave composta
            ItemCarrinhoId itemCarrinhoId = new ItemCarrinhoId();
            itemCarrinhoId.setCarrinhoId(carrinho.getId());
            itemCarrinhoId.setProdutoId(produto.getId());

            ItemCarrinho itemCarrinho = new ItemCarrinho();
            itemCarrinho.setId(itemCarrinhoId);
            itemCarrinho.setCarrinho(carrinho);
            itemCarrinho.setProduto(produto);
            itemCarrinho.setPrecoUnitario(BigDecimal.valueOf(100.25));
            itemCarrinho.setQuantidade(50);

            itemCarrinhoDAO.save(itemCarrinho);

            System.out.println("✅ Item do carrinho salvo com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}