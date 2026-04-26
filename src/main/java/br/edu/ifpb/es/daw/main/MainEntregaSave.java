package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.EntregaDAO;
import br.edu.ifpb.es.daw.dao.PedidoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.EntregaDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.PedidoDAOImpl;
import br.edu.ifpb.es.daw.entities.Entrega;
import br.edu.ifpb.es.daw.entities.Pedido;
import br.edu.ifpb.es.daw.entities.StatusEntrega;

import java.util.List;

public class MainEntregaSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            EntregaDAO entregaDAO = new EntregaDAOImpl();
            PedidoDAO pedidoDAO = new PedidoDAOImpl();

            List<Pedido> pedidos = pedidoDAO.findAll();

            System.out.println("Pedidos no banco: " + pedidos.size());

            Pedido pedido = pedidos.isEmpty() ? null : pedidos.get(0);

            System.out.println("ID do Pedido encontrado: " + (pedido != null ? pedido.getId() : "NULL"));

            if (pedido == null) {
                System.out.println("❌ Não há pedidos no banco! Execute MainPedidoSave primeiro.");
                return;
            }

            Entrega entrega = new Entrega();
            entrega.setTransportadora("Correios");
            entrega.setCodigoRastreamento("BR" + System.nanoTime());
            entrega.setStatusEntrega(StatusEntrega.SAIU_PARA_ENTREGA);
            entrega.setPedido(pedido);

            entregaDAO.save(entrega);

            System.out.println("✅ Entrega salva com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}