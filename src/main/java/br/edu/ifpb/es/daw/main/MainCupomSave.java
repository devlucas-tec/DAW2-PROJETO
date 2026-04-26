package br.edu.ifpb.es.daw.main;

import br.edu.ifpb.es.daw.dao.CupomDAO;
import br.edu.ifpb.es.daw.dao.impl.AbstractDAOImpl;
import br.edu.ifpb.es.daw.dao.impl.CupomDAOImpl;
import br.edu.ifpb.es.daw.entities.Cupom;
import br.edu.ifpb.es.daw.entities.StatusCupom;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MainCupomSave {

    public static void main(String[] args) {

        try {

            AbstractDAOImpl.initialize("daw");

            CupomDAO dao = new CupomDAOImpl();

            Cupom cupom = new Cupom();

            cupom.setCodigo("C" + (System.nanoTime() % 1000000000));
            cupom.setValorDesconto(new BigDecimal("50.00"));
            cupom.setDataExpiracao(LocalDate.now().plusDays(30));
            cupom.setStatus(StatusCupom.ATIVO);

            dao.save(cupom);

            System.out.println("✅ Cupom salvo com sucesso!");

        } finally {

            AbstractDAOImpl.closeFactory();

        }
    }
}