package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.PersistenciaDawException;
import br.edu.ifpb.es.daw.dao.ProdutoDAO;
import br.edu.ifpb.es.daw.entities.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.util.List;

public class ProdutoDAOImpl extends AbstractDAOImpl<Produto> implements ProdutoDAO {

    public ProdutoDAOImpl() {
        super(Produto.class);
    }

    // 2.1 - Consulta parametrizada com um parâmetro de tipo primitivo/wrapper (String)
    @Override
    public List<Produto> findByNome(String nome) throws PersistenciaDawException {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Produto> query = em.createQuery(
                    "SELECT p FROM Produto p WHERE p.nome LIKE :nome", Produto.class);
            query.setParameter("nome", "%" + nome + "%");
            return query.getResultList();
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            throw new PersistenciaDawException("Ocorreu algum erro ao tentar recuperar produtos pelo nome.", pe);
        }
    }

    // 2.3 - Consulta parametrizada com dois parâmetros de tipos primitivos/wrappers (BigDecimal)
    @Override
    public List<Produto> findByFaixaDePreco(BigDecimal precoMin, BigDecimal precoMax) throws PersistenciaDawException {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Produto> query = em.createQuery(
                    "SELECT p FROM Produto p WHERE p.preco BETWEEN :precoMin AND :precoMax", Produto.class);
            query.setParameter("precoMin", precoMin);
            query.setParameter("precoMax", precoMax);
            return query.getResultList();
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            throw new PersistenciaDawException("Ocorreu algum erro ao tentar recuperar produtos pela faixa de preço.", pe);
        }
    }

}