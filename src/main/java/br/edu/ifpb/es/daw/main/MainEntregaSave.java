package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.EntregaDAO;
import br.edu.ifpb.es.daw.dao.impl.EntregaDAOImpl;
import br.edu.ifpb.es.daw.entities.Entrega;
import br.edu.ifpb.es.daw.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;

public class MainEntregaSave {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        EntregaDAO dao = new EntregaDAOImpl(em);

        Entrega entrega = new Entrega();

        entrega.setTransportadora("Correios");

        // 🔥 CAMPO ÚNICO
        entrega.setCodigoRastreamento("BR" + System.nanoTime());

        entrega.setStatusEntrega("ENVIADO");

        entrega.setDataEnvio(LocalDateTime.now());
        entrega.setDataEntregaPrevista(LocalDateTime.now().plusDays(7));

        dao.save(entrega);

        em.close();

        System.out.println("Entrega salva com sucesso!");
    }
}