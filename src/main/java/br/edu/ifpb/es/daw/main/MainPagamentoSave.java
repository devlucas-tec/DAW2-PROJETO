package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.PagamentoDAO;
import br.edu.ifpb.es.daw.dao.impl.PagamentoDAOImpl;
import br.edu.ifpb.es.daw.entities.MetodoPagamento;
import br.edu.ifpb.es.daw.entities.Pagamento;

import br.edu.ifpb.es.daw.entities.StatusPagamento;
import br.edu.ifpb.es.daw.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;

public class MainPagamentoSave {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        PagamentoDAO dao = new PagamentoDAOImpl(em);

        Pagamento pagamento = new Pagamento();

        pagamento.setMetodo(MetodoPagamento.PIX);
        pagamento.setStatus(StatusPagamento.APROVADO);
        pagamento.setValorPago(new BigDecimal("250.00"));

        dao.save(pagamento);

        em.close();

        System.out.println("Pagamento salvo com sucesso!");
    }
}