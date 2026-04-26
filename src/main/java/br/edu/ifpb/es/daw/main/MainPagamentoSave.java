package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.PagamentoDAO;
import br.edu.ifpb.es.daw.dao.PedidoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.PagamentoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.PedidoDAOImpl;
import br.edu.ifpb.es.daw.entities.MetodoPagamento;
import br.edu.ifpb.es.daw.entities.Pagamento;
import br.edu.ifpb.es.daw.entities.Pedido;
import br.edu.ifpb.es.daw.entities.StatusPagamento;

import java.math.BigDecimal;
import java.util.List;

public class MainPagamentoSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            PagamentoDAO pagamentoDAO = new PagamentoDAOImpl();
            PedidoDAO pedidoDAO = new PedidoDAOImpl();

            List<Pedido> pedidos = pedidoDAO.findAll();

            System.out.println("Pedidos no banco: " + pedidos.size());

            Pedido pedido = pedidos.isEmpty() ? null : pedidos.get(0);

            System.out.println("ID do Pedido encontrado: " + (pedido != null ? pedido.getId() : "NULL"));

            if (pedido == null) {
                System.out.println("❌ Não há pedidos no banco! Execute MainPedidoSave primeiro.");
                return;
            }

            Pagamento pagamento = new Pagamento();
            pagamento.setMetodo(MetodoPagamento.PIX);
            pagamento.setStatus(StatusPagamento.APROVADO);
            pagamento.setValorPago(new BigDecimal("250.00"));
            pagamento.setPedido(pedido);

            pagamentoDAO.save(pagamento);

            System.out.println("✅ Pagamento salvo com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}