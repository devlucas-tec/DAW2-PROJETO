package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.VendedorDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.VendedorDAOImpl;
import br.edu.ifpb.es.daw.entities.Vendedor;

public class MainVendedorSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            VendedorDAO dao = new VendedorDAOImpl();

            Vendedor vendedor = new Vendedor();

            vendedor.setRazaoSocial("Empresa Teste");

            vendedor.setCnpjCpf("V" + (System.nanoTime() % 1000000000));

            dao.save(vendedor);

            System.out.println("Vendedor salvo com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}