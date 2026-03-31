package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.VendedorDAO;
import br.edu.ifpb.es.daw.dao.impl.VendedorDAOImpl;
import br.edu.ifpb.es.daw.entities.Vendedor;

import br.edu.ifpb.es.daw.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class MainVendedorSave {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        VendedorDAO dao = new VendedorDAOImpl(em);

        Vendedor vendedor = new Vendedor();

        vendedor.setRazaoSocial("Empresa Teste");

        vendedor.setCnpjCpf("V" + (System.nanoTime() % 1000000000));

        dao.save(vendedor);

        em.close();

        System.out.println("Vendedor salvo com sucesso!");
    }
}