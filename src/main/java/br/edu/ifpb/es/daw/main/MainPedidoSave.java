package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.PedidoDAO;
import br.edu.ifpb.es.daw.dao.impl.PedidoDAOImpl;
import br.edu.ifpb.es.daw.entities.Pedido;
import br.edu.ifpb.es.daw.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.math.BigDecimal;

public class MainPedidoSave {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        PedidoDAO dao = new PedidoDAOImpl(em);

        Pedido pedido = new Pedido();

        pedido.setValorTotal(new BigDecimal("500.00"));
        pedido.setStatus("CRIADO");

        dao.save(pedido);

        em.close();

        System.out.println("Pedido salvo com sucesso!");
    }
}