package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.impl.CarrinhoDAO;
import br.edu.ifpb.es.daw.dao.impl.CarrinhoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ItemPedidoDAO;
import br.edu.ifpb.es.daw.dao.impl.ItemPedidoDAOImpl;
import br.edu.ifpb.es.daw.entities.Carrinho;
import br.edu.ifpb.es.daw.entities.ItemPedido;
import br.edu.ifpb.es.daw.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;

public class MainItemPedidoSave {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        ItemPedidoDAO dao = new ItemPedidoDAOImpl(em);

        ItemPedido itemPedido = new ItemPedido();

        itemPedido.setPrecoUnitario(BigDecimal.valueOf(50.50));

        itemPedido.setQuantidade(30);

        dao.save(itemPedido);

        em.close();

        System.out.println("Item pedido salvo com sucesso");
    }
}
