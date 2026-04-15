package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.ItemPedidoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ItemPedidoDAOImpl;
import br.edu.ifpb.es.daw.entities.ItemPedido;

import java.math.BigDecimal;

public class MainItemPedidoSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            ItemPedidoDAO dao = new ItemPedidoDAOImpl();

            ItemPedido itemPedido = new ItemPedido();

            itemPedido.setPrecoUnitario(BigDecimal.valueOf(50.50));

            itemPedido.setQuantidade(30);

            dao.save(itemPedido);

            System.out.println("Item pedido salvo com sucesso");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}
