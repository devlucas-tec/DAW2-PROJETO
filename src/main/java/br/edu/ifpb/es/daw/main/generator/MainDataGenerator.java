package br.edu.ifpb.es.daw.main.generator;

import br.edu.ifpb.es.daw.dao.AvaliacaoDAO;
import br.edu.ifpb.es.daw.dao.CategoriaDAO;
import br.edu.ifpb.es.daw.dao.ClienteDAO;
import br.edu.ifpb.es.daw.dao.ItemPedidoDAO;
import br.edu.ifpb.es.daw.dao.PedidoDAO;
import br.edu.ifpb.es.daw.dao.ProdutoDAO;
import br.edu.ifpb.es.daw.dao.VendedorDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.AvaliacaoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.CategoriaDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ClienteDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ItemPedidoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.PedidoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ProdutoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.VendedorDAOImpl;
import br.edu.ifpb.es.daw.entities.Avaliacao;
import br.edu.ifpb.es.daw.entities.Categoria;
import br.edu.ifpb.es.daw.entities.Cliente;
import br.edu.ifpb.es.daw.entities.ItemPedido;
import br.edu.ifpb.es.daw.entities.ItemPedidoId;
import br.edu.ifpb.es.daw.entities.Pedido;
import br.edu.ifpb.es.daw.entities.Produto;
import br.edu.ifpb.es.daw.entities.StatusPedido;
import br.edu.ifpb.es.daw.entities.Vendedor;

import java.math.BigDecimal;

public class MainDataGenerator {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            CategoriaDAO  categoriaDAO  = new CategoriaDAOImpl();
            VendedorDAO   vendedorDAO   = new VendedorDAOImpl();
            ProdutoDAO    produtoDAO    = new ProdutoDAOImpl();
            ClienteDAO    clienteDAO    = new ClienteDAOImpl();
            PedidoDAO     pedidoDAO     = new PedidoDAOImpl();
            ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAOImpl();
            AvaliacaoDAO  avaliacaoDAO  = new AvaliacaoDAOImpl();


            System.out.println(">>> Salvando Categorias...");

            Categoria catEletronicos = new Categoria();
            catEletronicos.setNome("Eletronicos");
            catEletronicos.setDescricao("Produtos eletronicos em geral");
            categoriaDAO.save(catEletronicos);

            Categoria catRoupas = new Categoria();
            catRoupas.setNome("Roupas");
            catRoupas.setDescricao("Vestuario masculino e feminino");
            categoriaDAO.save(catRoupas);

            Categoria catLivros = new Categoria();
            catLivros.setNome("Livros");
            catLivros.setDescricao("Livros de diversas areas do conhecimento");
            categoriaDAO.save(catLivros);

            System.out.println(" 3 categorias salvas.");

            System.out.println(" Salvando Vendedores...");

            Vendedor vendedor1 = new Vendedor();
            vendedor1.setRazaoSocial("TechStore Ltda");
            vendedor1.setCnpjCpf("12.345.678/0001-90");
            vendedorDAO.save(vendedor1);

            Vendedor vendedor2 = new Vendedor();
            vendedor2.setRazaoSocial("Moda Express ME");
            vendedor2.setCnpjCpf("98.765.432/0001-10");
            vendedorDAO.save(vendedor2);

            System.out.println(" 2 vendedores salvos.");

            System.out.println("Salvando Produtos...");

            Produto notebook = new Produto();
            notebook.setNome("Notebook Dell i7");
            notebook.setDescricao("Notebook com processador Intel i7, 16GB RAM");
            notebook.setEstoque(10);
            notebook.setPreco(new BigDecimal("3500.00"));
            notebook.setCategoria(catEletronicos);
            notebook.setVendedor(vendedor1);
            produtoDAO.save(notebook);

            Produto notebook2 = new Produto();
            notebook2.setNome("Notebook Lenovo i5");
            notebook2.setDescricao("Notebook com processador Intel i5, 8GB RAM");
            notebook2.setEstoque(15);
            notebook2.setPreco(new BigDecimal("2800.00"));
            notebook2.setCategoria(catEletronicos);
            notebook2.setVendedor(vendedor1);
            produtoDAO.save(notebook2);

            Produto fone = new Produto();
            fone.setNome("Fone de Ouvido Bluetooth");
            fone.setDescricao("Fone sem fio com cancelamento de ruido");
            fone.setEstoque(50);
            fone.setPreco(new BigDecimal("250.00"));
            fone.setCategoria(catEletronicos);
            fone.setVendedor(vendedor1);
            produtoDAO.save(fone);

            Produto camiseta = new Produto();
            camiseta.setNome("Camiseta Polo Premium");
            camiseta.setDescricao("Camiseta polo 100% algodao");
            camiseta.setEstoque(100);
            camiseta.setPreco(new BigDecimal("89.90"));
            camiseta.setCategoria(catRoupas);
            camiseta.setVendedor(vendedor2);
            produtoDAO.save(camiseta);

            Produto livroJava = new Produto();
            livroJava.setNome("Livro Java Efetivo");
            livroJava.setDescricao("Guia completo de boas praticas em Java");
            livroJava.setEstoque(30);
            livroJava.setPreco(new BigDecimal("120.00"));
            livroJava.setCategoria(catLivros);
            livroJava.setVendedor(vendedor2);
            produtoDAO.save(livroJava);

            System.out.println("    5 produtos salvos.");

            System.out.println(">>> Salvando Clientes...");

            Cliente cliente1 = new Cliente();
            cliente1.setNome("Ana Paula");
            cliente1.setEmail("ana.paula@email.com");
            cliente1.setSenha("senha123");
            cliente1.setTelefone("83911111111");
            clienteDAO.save(cliente1);

            Cliente cliente2 = new Cliente();
            cliente2.setNome("Carlos Eduardo");
            cliente2.setEmail("carlos.edu@email.com");
            cliente2.setSenha("senha456");
            cliente2.setTelefone("83922222222");
            clienteDAO.save(cliente2);

            Cliente cliente3 = new Cliente();
            cliente3.setNome("Mariana Lima");
            cliente3.setEmail("mariana.lima@email.com");
            cliente3.setSenha("senha789");
            cliente3.setTelefone("83933333333");
            clienteDAO.save(cliente3);

            System.out.println("    3 clientes salvos.");

            System.out.println(">>> Salvando Pedidos...");

            Pedido pedido1 = new Pedido();
            pedido1.setCliente(cliente1);
            pedido1.setValorTotal(new BigDecimal("6300.00"));
            pedido1.setStatus(StatusPedido.CONFIRMADO);
            pedidoDAO.save(pedido1);

            Pedido pedido2 = new Pedido();
            pedido2.setCliente(cliente2);
            pedido2.setValorTotal(new BigDecimal("2920.00"));
            pedido2.setStatus(StatusPedido.CRIADO);
            pedidoDAO.save(pedido2);

            Pedido pedido3 = new Pedido();
            pedido3.setCliente(cliente3);
            pedido3.setValorTotal(new BigDecimal("209.90"));
            pedido3.setStatus(StatusPedido.CONFIRMADO);
            pedidoDAO.save(pedido3);

            System.out.println("    3 pedidos salvos.");

            System.out.println(">>> Salvando Itens de Pedido...");

            ItemPedido item1p1 = new ItemPedido();
            item1p1.setId(new ItemPedidoId(pedido1.getId(), notebook.getId()));
            item1p1.setPedido(pedido1);
            item1p1.setProduto(notebook);
            item1p1.setQuantidade(1);
            item1p1.setPrecoUnitario(new BigDecimal("3500.00"));
            itemPedidoDAO.save(item1p1);

            ItemPedido item2p1 = new ItemPedido();
            item2p1.setId(new ItemPedidoId(pedido1.getId(), fone.getId()));
            item2p1.setPedido(pedido1);
            item2p1.setProduto(fone);
            item2p1.setQuantidade(2);
            item2p1.setPrecoUnitario(new BigDecimal("250.00"));
            itemPedidoDAO.save(item2p1);

            ItemPedido item1p2 = new ItemPedido();
            item1p2.setId(new ItemPedidoId(pedido2.getId(), notebook2.getId()));
            item1p2.setPedido(pedido2);
            item1p2.setProduto(notebook2);
            item1p2.setQuantidade(1);
            item1p2.setPrecoUnitario(new BigDecimal("2800.00"));
            itemPedidoDAO.save(item1p2);

            ItemPedido item2p2 = new ItemPedido();
            item2p2.setId(new ItemPedidoId(pedido2.getId(), livroJava.getId()));
            item2p2.setPedido(pedido2);
            item2p2.setProduto(livroJava);
            item2p2.setQuantidade(1);
            item2p2.setPrecoUnitario(new BigDecimal("120.00"));
            itemPedidoDAO.save(item2p2);

            ItemPedido item1p3 = new ItemPedido();
            item1p3.setId(new ItemPedidoId(pedido3.getId(), fone.getId()));
            item1p3.setPedido(pedido3);
            item1p3.setProduto(fone);
            item1p3.setQuantidade(1);
            item1p3.setPrecoUnitario(new BigDecimal("250.00"));
            itemPedidoDAO.save(item1p3);

            ItemPedido item2p3 = new ItemPedido();
            item2p3.setId(new ItemPedidoId(pedido3.getId(), camiseta.getId()));
            item2p3.setPedido(pedido3);
            item2p3.setProduto(camiseta);
            item2p3.setQuantidade(1);
            item2p3.setPrecoUnitario(new BigDecimal("89.90"));
            itemPedidoDAO.save(item2p3);

            System.out.println("    6 itens de pedido salvos (2 por pedido).");

            System.out.println(">>> Salvando Avaliacoes...");

            Avaliacao av1 = new Avaliacao();
            av1.setNota(5);
            av1.setComentario("Excelente notebook, muito rapido!");
            av1.setCliente(cliente1);
            av1.setProduto(notebook);
            avaliacaoDAO.save(av1);

            Avaliacao av2 = new Avaliacao();
            av2.setNota(4);
            av2.setComentario("Muito bom, mas esquenta um pouco.");
            av2.setCliente(cliente2);
            av2.setProduto(notebook);
            avaliacaoDAO.save(av2);

            Avaliacao av3 = new Avaliacao();
            av3.setNota(3);
            av3.setComentario("Bom custo-beneficio, mas poderia ter mais RAM.");
            av3.setCliente(cliente3);
            av3.setProduto(notebook);
            avaliacaoDAO.save(av3);

            System.out.println("3 avaliacoes salvas (notas 5, 4 e 3 - media esperada: 4.0).");

            System.out.println(" ");
            System.out.println(" ");
            System.out.println("  MainDataGenerator concluido com sucesso!");
            System.out.println("  Dados inseridos:");
            System.out.println("    - 3 Categorias");
            System.out.println("    - 2 Vendedores");
            System.out.println("    - 5 Produtos");
            System.out.println("    - 3 Clientes");
            System.out.println("    - 3 Pedidos");
            System.out.println("    - 6 ItensPedido");
            System.out.println("    - 3 Avaliacoes");
            System.out.println(" ");

        } finally {

            AbstractDAOImpl.closeFactory();

        }

    }

}