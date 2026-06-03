package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.AvaliacaoDAO;
import br.edu.ifpb.es.daw.dao.PersistenciaDawException;
import br.edu.ifpb.es.daw.model.Avaliacao;
import br.edu.ifpb.es.daw.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

public class AvaliacaoDAOImpl extends AbstractDAOImpl<Avaliacao> implements AvaliacaoDAO {

    public AvaliacaoDAOImpl() {
        super(Avaliacao.class);
    }

    // 2.4 - Consulta que faz uso de função de agregação (AVG)
    @Override
    public Double calcularMediaNotasPorProduto(Produto produto) throws PersistenciaDawException {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Double> query = em.createQuery(
                    "SELECT AVG(a.nota) FROM Avaliacao a WHERE a.produto = :produto", Double.class);
            query.setParameter("produto", produto);
            return query.getSingleResult();
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            throw new PersistenciaDawException("Ocorreu algum erro ao tentar calcular a média das notas do produto informado.", pe);
        }
    }

}