package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.ClienteDAO;
import br.edu.ifpb.es.daw.dao.EnderecoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ClienteDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.EnderecoDAOImpl;
import br.edu.ifpb.es.daw.entities.Cliente;
import br.edu.ifpb.es.daw.entities.Endereco;

import java.util.List;

public class MainEnderecoSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            EnderecoDAO enderecoDAO = new EnderecoDAOImpl();
            ClienteDAO clienteDAO = new ClienteDAOImpl();

            List<Cliente> clientes = clienteDAO.findAll();

            System.out.println("Clientes no banco: " + clientes.size());

            Cliente cliente = clientes.isEmpty() ? null : clientes.get(0);

            System.out.println("ID do Cliente encontrado: " + (cliente != null ? cliente.getId() : "NULL"));

            if (cliente == null) {
                System.out.println("❌ Não há clientes no banco! Execute MainClienteSave primeiro.");
                return;
            }

            Endereco endereco = new Endereco();
            endereco.setRua("Rua das Flores");
            endereco.setNumero("123");
            endereco.setComplemento("Apto 101");
            endereco.setCep("58000000");
            endereco.setCidade("João Pessoa");
            endereco.setEstado("PB");
            endereco.setCliente(cliente);

            enderecoDAO.save(endereco);

            System.out.println("✅ Endereço salvo com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }

    }
}