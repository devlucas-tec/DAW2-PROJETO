package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.ClienteDAO;
import br.edu.ifpb.es.daw.dao.PedidoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ClienteDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.PedidoDAOImpl;
import br.edu.ifpb.es.daw.entities.Cliente;
import br.edu.ifpb.es.daw.entities.Pedido;
import br.edu.ifpb.es.daw.entities.StatusPedido;

import java.math.BigDecimal;
import java.util.List;

public class MainPedidoSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            PedidoDAO pedidoDAO = new PedidoDAOImpl();
            ClienteDAO clienteDAO = new ClienteDAOImpl();

            List<Cliente> clientes = clienteDAO.findAll();

            System.out.println("Clientes no banco: " + clientes.size());

            Cliente cliente = clientes.isEmpty() ? null : clientes.get(0);

            System.out.println("ID do Cliente encontrado: " + (cliente != null ? cliente.getId() : "NULL"));

            if (cliente == null) {
                System.out.println("❌ Não há clientes no banco!");
                return;
            }

            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            pedido.setValorTotal(new BigDecimal("500.00"));
            pedido.setStatus(StatusPedido.CRIADO);

            pedidoDAO.save(pedido);

            System.out.println("✅ Pedido salvo com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}