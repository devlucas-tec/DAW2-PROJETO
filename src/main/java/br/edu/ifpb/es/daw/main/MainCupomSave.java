package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.CupomDAO;
import br.edu.ifpb.es.daw.dao.impl.CupomDAOImpl;
import br.edu.ifpb.es.daw.entities.Cupom;
import br.edu.ifpb.es.daw.entities.StatusEnum;

import br.edu.ifpb.es.daw.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MainCupomSave {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();

        CupomDAO dao = new CupomDAOImpl(em);

        Cupom cupom = new Cupom();

        cupom.setCodigo("C" + (System.nanoTime() % 1000000000));

        cupom.setValorDesconto(new BigDecimal("50.00"));
        cupom.setDataExpiracao(LocalDate.now().plusDays(30));
        cupom.setStatus(StatusEnum.ATIVO);

        dao.save(cupom);

        em.close();

        System.out.println("Cupom salvo com sucesso!");
    }
}