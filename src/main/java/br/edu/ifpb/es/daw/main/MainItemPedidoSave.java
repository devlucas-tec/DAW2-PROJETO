package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.ItemPedidoDAO;
import br.edu.ifpb.es.daw.dao.PedidoDAO;
import br.edu.ifpb.es.daw.dao.ProdutoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ItemPedidoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.PedidoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ProdutoDAOImpl;
import br.edu.ifpb.es.daw.entities.ItemPedido;
import br.edu.ifpb.es.daw.entities.ItemPedidoId;
import br.edu.ifpb.es.daw.entities.Pedido;
import br.edu.ifpb.es.daw.entities.Produto;

import java.math.BigDecimal;
import java.util.List;

public class MainItemPedidoSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAOImpl();
            PedidoDAO pedidoDAO = new PedidoDAOImpl();
            ProdutoDAO produtoDAO = new ProdutoDAOImpl();

            List<Pedido> pedidos = pedidoDAO.findAll();
            List<Produto> produtos = produtoDAO.findAll();

            System.out.println("Pedidos no banco: " + pedidos.size());
            System.out.println("Produtos no banco: " + produtos.size());

            Pedido pedido = pedidos.isEmpty() ? null : pedidos.get(0);
            Produto produto = produtos.isEmpty() ? null : produtos.get(0);

            System.out.println("ID do Pedido encontrado: " + (pedido != null ? pedido.getId() : "NULL"));
            System.out.println("ID do Produto encontrado: " + (produto != null ? produto.getId() : "NULL"));

            if (pedido == null || produto == null) {
                System.out.println("❌ Não há pedidos ou produtos no banco!");
                return;
            }

            // Verifica se o item já existe no pedido
            ItemPedido existente = itemPedidoDAO.findByPedidoAndProduto(pedido.getId(), produto.getId());

            if (existente != null) {
                existente.setQuantidade(existente.getQuantidade() + 1);
                itemPedidoDAO.update(existente);
                System.out.println("⚠️ Item já existia no pedido. Quantidade atualizada para: " + existente.getQuantidade());
                return;
            }

            // Monta a chave composta
            ItemPedidoId itemPedidoId = new ItemPedidoId(pedido.getId(), produto.getId());

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setId(itemPedidoId);
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(produto);
            itemPedido.setPrecoUnitario(BigDecimal.valueOf(50.50));
            itemPedido.setQuantidade(30);

            itemPedidoDAO.save(itemPedido);

            System.out.println("✅ Item do pedido salvo com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}