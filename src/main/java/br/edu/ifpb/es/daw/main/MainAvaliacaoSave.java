package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.AvaliacaoDAO;
import br.edu.ifpb.es.daw.dao.ClienteDAO;
import br.edu.ifpb.es.daw.dao.ProdutoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.AvaliacaoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ClienteDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ProdutoDAOImpl;
import br.edu.ifpb.es.daw.entities.Avaliacao;
import br.edu.ifpb.es.daw.entities.Cliente;
import br.edu.ifpb.es.daw.entities.Produto;

import java.util.List;

public class MainAvaliacaoSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAOImpl();
            ClienteDAO clienteDAO = new ClienteDAOImpl();
            ProdutoDAO produtoDAO = new ProdutoDAOImpl();

            List<Cliente> clientes = clienteDAO.findAll();
            List<Produto> produtos = produtoDAO.findAll();

            System.out.println("Clientes no banco: " + clientes.size());
            System.out.println("Produtos no banco: " + produtos.size());

            Cliente cliente = clientes.isEmpty() ? null : clientes.get(0);
            Produto produto = produtos.isEmpty() ? null : produtos.get(0);

            System.out.println("ID do Cliente encontrado: " + (cliente != null ? cliente.getId() : "NULL"));
            System.out.println("ID do Produto encontrado: " + (produto != null ? produto.getId() : "NULL"));

            if (cliente == null || produto == null) {
                System.out.println("❌ Não há clientes ou produtos no banco!");
                return;
            }

            Avaliacao avaliacao = new Avaliacao();
            avaliacao.setNota(5);
            avaliacao.setComentario("Muito bom!");
            avaliacao.setCliente(cliente);
            avaliacao.setProduto(produto);

            avaliacaoDAO.save(avaliacao);

            System.out.println("Avaliação salva com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}