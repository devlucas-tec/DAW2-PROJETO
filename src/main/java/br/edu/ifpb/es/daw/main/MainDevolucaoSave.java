package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.DevolucaoDAO;
import br.edu.ifpb.es.daw.dao.PedidoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.DevolucaoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.PedidoDAOImpl;
import br.edu.ifpb.es.daw.entities.Devolucao;
import br.edu.ifpb.es.daw.entities.Pedido;
import br.edu.ifpb.es.daw.entities.StatusDevolucao;

import java.util.List;

public class MainDevolucaoSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            DevolucaoDAO devolucaoDAO = new DevolucaoDAOImpl();
            PedidoDAO pedidoDAO = new PedidoDAOImpl();

            List<Pedido> pedidos = pedidoDAO.findAll();

            System.out.println("Pedidos no banco: " + pedidos.size());

            Pedido pedido = pedidos.isEmpty() ? null : pedidos.get(0);

            System.out.println("ID do Pedido encontrado: " + (pedido != null ? pedido.getId() : "NULL"));

            if (pedido == null) {
                System.out.println("❌ Não há pedidos no banco! Execute MainPedidoSave primeiro.");
                return;
            }

            Devolucao devolucao = new Devolucao();
            devolucao.setMotivo("Produto com defeito");
            devolucao.setStatus(StatusDevolucao.APROVADA);
            devolucao.setPedido(pedido);

            devolucaoDAO.save(devolucao);

            System.out.println("✅ Devolução salva com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}