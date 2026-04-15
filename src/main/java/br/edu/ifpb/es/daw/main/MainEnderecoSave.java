package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.EnderecoDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.EnderecoDAOImpl;
import br.edu.ifpb.es.daw.entities.Endereco;

public class MainEnderecoSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            EnderecoDAO dao = new EnderecoDAOImpl();

            Endereco endereco = new Endereco();

            endereco.setRua("Rua das Flores");
            endereco.setNumero("123");
            endereco.setComplemento("Apto 101");
            endereco.setCep("58000000");
            endereco.setCidade("João Pessoa");
            endereco.setEstado("PB");

            dao.save(endereco);

            System.out.println("Endereço salvo com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }

    }
}