package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.EntregaDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.EntregaDAOImpl;
import br.edu.ifpb.es.daw.entities.Entrega;
import br.edu.ifpb.es.daw.entities.StatusEntrega;

import java.time.LocalDateTime;

public class MainEntregaSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            EntregaDAO dao = new EntregaDAOImpl();

            Entrega entrega = new Entrega();

            entrega.setTransportadora("Correios");

            entrega.setCodigoRastreamento("BR" + System.nanoTime());

            entrega.setStatusEntrega(StatusEntrega.SAIU_PARA_ENTREGA);

            entrega.setDataEnvio(LocalDateTime.now());
            entrega.setDataEntregaPrevista(LocalDateTime.now().plusDays(7));

            dao.save(entrega);

            System.out.println("Entrega salva com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}