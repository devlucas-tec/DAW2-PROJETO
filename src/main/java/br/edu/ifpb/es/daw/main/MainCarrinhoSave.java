package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.CarrinhoDAO;
import br.edu.ifpb.es.daw.dao.ClienteDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.CarrinhoDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ClienteDAOImpl;
import br.edu.ifpb.es.daw.entities.Carrinho;
import br.edu.ifpb.es.daw.entities.Cliente;

import java.util.List;

public class MainCarrinhoSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            CarrinhoDAO carrinhoDAO = new CarrinhoDAOImpl();
            ClienteDAO clienteDAO = new ClienteDAOImpl();

            List<Cliente> clientes = clienteDAO.findAll();

            System.out.println("Clientes no banco: " + clientes.size());

            Cliente cliente = clientes.isEmpty() ? null : clientes.get(0);

            if (cliente == null) {
                System.out.println("❌ Não há clientes no banco!");
                return;
            }

            System.out.println("ID do Cliente encontrado: " + cliente.getId());

            // Verifica se o cliente já possui carrinho
            List<Carrinho> carrinhos = carrinhoDAO.findAll();
            boolean jaTemCarrinho = carrinhos.stream()
                    .anyMatch(c -> c.getCliente().getId().equals(cliente.getId()));

            if (jaTemCarrinho) {
                System.out.println("⚠️ Cliente já possui um carrinho! Nenhum carrinho criado.");
                return;
            }

            Carrinho carrinho = new Carrinho();
            carrinho.setCliente(cliente);

            carrinhoDAO.save(carrinho);

            System.out.println("✅ Carrinho salvo com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}