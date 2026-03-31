package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.ClienteDAO;
import br.edu.ifpb.es.daw.dao.impl.ClienteDAOImpl;
import br.edu.ifpb.es.daw.entities.Cliente;
import br.edu.ifpb.es.daw.util.JPAUtil;

import jakarta.persistence.EntityManager;

public class MainClienteSave {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        ClienteDAO dao = new ClienteDAOImpl(em);

        Cliente cliente = new Cliente();

        cliente.setNome("João da Silva");

        cliente.setEmail("joao" + System.nanoTime() + "@gmail.com");

        cliente.setSenha("123456");
        cliente.setTelefone("83999999999");

        dao.save(cliente);

        em.close();

        System.out.println("Cliente salvo com sucesso!");
    }
}