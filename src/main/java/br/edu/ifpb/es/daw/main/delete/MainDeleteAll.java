package br.edu.ifpb.es.daw.main.delete;

import br.edu.ifpb.es.daw.dao.AvaliacaoDAO;
import br.edu.ifpb.es.daw.dao.CarrinhoDAO;
import br.edu.ifpb.es.daw.dao.CategoriaDAO;
import br.edu.ifpb.es.daw.dao.ClienteDAO;
import br.edu.ifpb.es.daw.dao.CupomDAO;
import br.edu.ifpb.es.daw.dao.DevolucaoDAO;
import br.edu.ifpb.es.daw.dao.EnderecoDAO;
import br.edu.ifpb.es.daw.dao.EntregaDAO;
import br.edu.ifpb.es.daw.dao.ItemCarrinhoDAO;
import br.edu.ifpb.es.daw.dao.ItemPedidoDAO;
import br.edu.ifpb.es.daw.dao.PagamentoDAO;
import br.edu.ifpb.es.daw.dao.PedidoDAO;
import br.edu.ifpb.es.daw.dao.ProdutoDAO;
import br.edu.ifpb.es.daw.dao.VendedorDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.AvaliacaoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.CarrinhoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.CategoriaDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ClienteDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.CupomDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.DevolucaoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.EnderecoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.EntregaDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ItemCarrinhoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ItemPedidoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.PagamentoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.PedidoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ProdutoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.VendedorDAOImpl;

public class MainDeleteAll {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            AvaliacaoDAO    avaliacaoDAO    = new AvaliacaoDAOImpl();
            ItemPedidoDAO   itemPedidoDAO   = new ItemPedidoDAOImpl();
            ItemCarrinhoDAO itemCarrinhoDAO = new ItemCarrinhoDAOImpl();
            PagamentoDAO    pagamentoDAO    = new PagamentoDAOImpl();
            EntregaDAO      entregaDAO      = new EntregaDAOImpl();
            DevolucaoDAO    devolucaoDAO    = new DevolucaoDAOImpl();
            PedidoDAO       pedidoDAO       = new PedidoDAOImpl();
            CarrinhoDAO     carrinhoDAO     = new CarrinhoDAOImpl();
            ProdutoDAO      produtoDAO      = new ProdutoDAOImpl();
            ClienteDAO      clienteDAO      = new ClienteDAOImpl();
            VendedorDAO     vendedorDAO     = new VendedorDAOImpl();
            CategoriaDAO    categoriaDAO    = new CategoriaDAOImpl();
            CupomDAO        cupomDAO        = new CupomDAOImpl();
            EnderecoDAO     enderecoDAO     = new EnderecoDAOImpl();

            System.out.println("Deletando Avaliacoes...");
            avaliacaoDAO.deleteAll();
            System.out.println("    Avaliacoes deletadas.");

            System.out.println(" ItensPedido...");
            itemPedidoDAO.deleteAll();
            System.out.println("    ItensPedido deletados.");

            System.out.println(" Deletando ItensCarrinho...");
            itemCarrinhoDAO.deleteAll();
            System.out.println("    ItensCarrinho deletados.");

            System.out.println(" Deletando Pagamentos...");
            pagamentoDAO.deleteAll();
            System.out.println("    Pagamentos deletados.");

            System.out.println(" Deletando Entregas...");
            entregaDAO.deleteAll();
            System.out.println("    Entregas deletadas.");

            System.out.println(" Deletando Devolucoes...");
            devolucaoDAO.deleteAll();
            System.out.println("    Devolucoes deletadas.");

            System.out.println(" Deletando Pedidos...");
            pedidoDAO.deleteAll();
            System.out.println("    Pedidos deletados.");

            System.out.println(">>> Deletando Carrinhos...");
            carrinhoDAO.deleteAll();
            System.out.println("    Carrinhos deletados.");

            System.out.println(" Deletando Produtos...");
            produtoDAO.deleteAll();
            System.out.println("    Produtos deletados.");

            System.out.println(" Deletando Clientes...");
            clienteDAO.deleteAll();
            System.out.println("    Clientes deletados.");

            System.out.println("Deletando Vendedores...");
            vendedorDAO.deleteAll();
            System.out.println("    Vendedores deletados.");

            System.out.println(" Deletando Categorias...");
            categoriaDAO.deleteAll();
            System.out.println("    Categorias deletadas.");

            System.out.println(" Deletando Cupons...");
            cupomDAO.deleteAll();
            System.out.println("    Cupons deletados.");

            System.out.println(" Deletando Enderecos...");
            enderecoDAO.deleteAll();
            System.out.println("    Enderecos deletados.");

            System.out.println("");
            System.out.println("");
            System.out.println("  MainDeleteAll concluido com sucesso!");
            System.out.println("  Todos os registros foram removidos.");
            System.out.println("");

        } finally {

            AbstractDAOImpl.closeFactory();

        }

    }

}