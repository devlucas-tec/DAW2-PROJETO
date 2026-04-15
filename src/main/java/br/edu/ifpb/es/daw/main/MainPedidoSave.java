package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.PedidoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.PedidoDAOImpl;
import br.edu.ifpb.es.daw.entities.Pedido;
import br.edu.ifpb.es.daw.entities.StatusPedido;

import java.math.BigDecimal;

public class MainPedidoSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            PedidoDAO dao = new PedidoDAOImpl();

            Pedido pedido = new Pedido();

            pedido.setValorTotal(new BigDecimal("500.00"));
            pedido.setStatus(StatusPedido.CRIADO);

            dao.save(pedido);

            System.out.println("Pedido salvo com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}