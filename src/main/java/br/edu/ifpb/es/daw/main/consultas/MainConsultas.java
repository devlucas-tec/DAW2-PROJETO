package br.edu.ifpb.es.daw.main.consultas;

import br.edu.ifpb.es.daw.dao.AvaliacaoDAO;
import br.edu.ifpb.es.daw.dao.ClienteDAO;
import br.edu.ifpb.es.daw.dao.PedidoDAO;
import br.edu.ifpb.es.daw.dao.PersistenciaDawException;
import br.edu.ifpb.es.daw.dao.ProdutoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.AvaliacaoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ClienteDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.PedidoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ProdutoDAOImpl;
import br.edu.ifpb.es.daw.entities.Cliente;
import br.edu.ifpb.es.daw.entities.Pedido;
import br.edu.ifpb.es.daw.entities.Produto;

import java.math.BigDecimal;
import java.util.List;

public class MainConsultas {

    public static void main(String[] args) throws PersistenciaDawException {

        try {

            AbstractDAOImpl.initialize("daw");

            ProdutoDAO produtoDAO = new ProdutoDAOImpl();
            PedidoDAO pedidoDAO = new PedidoDAOImpl();
            AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAOImpl();
            ClienteDAO clienteDAO = new ClienteDAOImpl();

            // ================================================================
            // 2.1 - Consulta parametrizada com um parâmetro primitivo/wrapper
            // Busca produtos cujo nome contenha o trecho informado (String)
            // ================================================================
            System.out.println("==============================");
            System.out.println("=== Consulta 2.1 - findByNome(\"Notebook\") ===");
            System.out.println("==============================");
            List<Produto> porNome = produtoDAO.findByNome("Notebook");
            if (porNome.isEmpty()) {
                System.out.println("Nenhum produto encontrado.");
            } else {
                porNome.forEach(p -> System.out.println("  -> " + p));
            }

            // ================================================================
            // 2.2 - Consulta parametrizada onde o parâmetro é uma entidade
            // Busca todos os pedidos de um cliente passado como objeto JPA
            // ================================================================
            System.out.println("\n==============================");
            System.out.println("=== Consulta 2.2 - findByCliente(cliente) ===");
            System.out.println("==============================");
            List<Cliente> clientes = clienteDAO.findAll();
            if (clientes.isEmpty()) {
                System.out.println("Nenhum cliente no banco. Execute o MainDataGenerator primeiro.");
            } else {
                Cliente cliente = clientes.get(0);
                System.out.println("Buscando pedidos do cliente: " + cliente.getNome());
                List<Pedido> porCliente = pedidoDAO.findByCliente(cliente);
                if (porCliente.isEmpty()) {
                    System.out.println("Nenhum pedido encontrado para esse cliente.");
                } else {
                    porCliente.forEach(p -> System.out.println("  -> " + p));
                }
            }

            // ================================================================
            // 2.3 - Consulta parametrizada com dois parâmetros primitivos/wrappers
            // Busca produtos com preço entre dois valores BigDecimal
            // ================================================================
            System.out.println("\n==============================");
            System.out.println("=== Consulta 2.3 - findByFaixaDePreco(100.00, 4000.00) ===");
            System.out.println("==============================");
            List<Produto> porFaixa = produtoDAO.findByFaixaDePreco(
                    new BigDecimal("100.00"), new BigDecimal("4000.00"));
            if (porFaixa.isEmpty()) {
                System.out.println("Nenhum produto encontrado na faixa de preço informada.");
            } else {
                porFaixa.forEach(p -> System.out.println("  -> " + p));
            }

            // ================================================================
            // 2.4 - Consulta com função de agregação (AVG)
            // Calcula a média das notas das avaliações do primeiro produto encontrado
            // ================================================================
            System.out.println("\n==============================");
            System.out.println("=== Consulta 2.4 - calcularMediaNotasPorProduto(produto) ===");
            System.out.println("==============================");
            List<Produto> produtos = produtoDAO.findAll();
            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto no banco. Execute o MainDataGenerator primeiro.");
            } else {
                Produto produto = produtos.get(0);
                Double media = avaliacaoDAO.calcularMediaNotasPorProduto(produto);
                System.out.println("  Média das notas do produto \"" + produto.getNome() + "\": " + media);
            }

            // ================================================================
            // 2.5 - Consulta com FETCH de relacionamento LAZY
            // Busca o primeiro pedido já com seus itens carregados via JOIN FETCH
            // ================================================================
            System.out.println("\n==============================");
            System.out.println("=== Consulta 2.5 - findByIdComItens(id) ===");
            System.out.println("==============================");
            List<Pedido> pedidos = pedidoDAO.findAll();
            if (pedidos.isEmpty()) {
                System.out.println("Nenhum pedido no banco. Execute o MainDataGenerator primeiro.");
            } else {
                Long idPedido = pedidos.get(0).getId();
                Pedido pedidoComItens = pedidoDAO.findByIdComItens(idPedido);
                System.out.println("  Pedido: " + pedidoComItens);
                System.out.println("  Itens carregados via JOIN FETCH:");
                pedidoComItens.getItens().forEach(i -> System.out.println("    -> " + i));
            }

        } finally {

            AbstractDAOImpl.closeFactory();

        }

    }

}