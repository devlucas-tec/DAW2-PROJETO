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

            Carrinho carrinho = carrinhos.isEmpty() ? null : carrinhos.get(0);
            Produto produto = produtos.isEmpty() ? null : produtos.get(0);

            if (carrinho == null || produto == null) {
                System.out.println("❌ Não há carrinhos ou produtos no banco!");
                return;
            }

            System.out.println("ID do Carrinho encontrado: " + carrinho.getId());
            System.out.println("ID do Produto encontrado: " + produto.getId());

            // Verifica se o item já existe no carrinho
            ItemCarrinho existente = itemCarrinhoDAO.findByCarrinhoAndProduto(carrinho.getId(), produto.getId());

            if (existente != null) {
                existente.setQuantidade(existente.getQuantidade() + 1);
                itemCarrinhoDAO.update(existente);
                System.out.println("⚠️ Item já existia no carrinho. Quantidade atualizada para: " + existente.getQuantidade());
                return;
            }

            ItemCarrinhoId itemCarrinhoId = new ItemCarrinhoId(carrinho.getId(), produto.getId());

            ItemCarrinho itemCarrinho = new ItemCarrinho();
            itemCarrinho.setId(itemCarrinhoId);
            itemCarrinho.setCarrinho(carrinho);
            itemCarrinho.setProduto(produto);
            itemCarrinho.setPrecoUnitario(BigDecimal.valueOf(100.25));
            itemCarrinho.setQuantidade(1);

            itemCarrinhoDAO.save(itemCarrinho);

            System.out.println("✅ Item do carrinho salvo com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}