package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.ClienteDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.ClienteDAOImpl;
import br.edu.ifpb.es.daw.entities.Cliente;

public class MainClienteSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            ClienteDAO dao = new ClienteDAOImpl();

            Cliente cliente = new Cliente();

            cliente.setNome("João da Silva");

            cliente.setEmail("joao" + System.nanoTime() + "@gmail.com");

            cliente.setSenha("123456");
            cliente.setTelefone("83999999999");

            dao.save(cliente);

            System.out.println("Cliente salvo com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }

    }
}