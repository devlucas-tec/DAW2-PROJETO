package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.PersistenciaDawException;
import br.edu.ifpb.es.daw.dao.PedidoDAO;
import br.edu.ifpb.es.daw.entities.Cliente;
import br.edu.ifpb.es.daw.entities.Pedido;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PedidoDAOImpl extends AbstractDAOImpl<Pedido> implements PedidoDAO {

    public PedidoDAOImpl() {
        super(Pedido.class);
    }

    // 2.2 - Consulta parametrizada onde o parâmetro é uma entidade (Cliente)
    @Override
    public List<Pedido> findByCliente(Cliente cliente) throws PersistenciaDawException {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Pedido> query = em.createQuery(
                    "SELECT p FROM Pedido p WHERE p.cliente = :cliente", Pedido.class);
            query.setParameter("cliente", cliente);
            return query.getResultList();
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            throw new PersistenciaDawException("Ocorreu algum erro ao tentar recuperar os pedidos do cliente informado.", pe);
        }
    }

    // 2.5 - Consulta que faz FETCH de relacionamento LAZY (itens do pedido)
    @Override
    public Pedido findByIdComItens(Long id) throws PersistenciaDawException {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Pedido> query = em.createQuery(
                    "SELECT DISTINCT p FROM Pedido p JOIN FETCH p.itens WHERE p.id = :id", Pedido.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            throw new PersistenciaDawException("Ocorreu algum erro ao tentar recuperar o pedido com seus itens pelo id.", pe);
        }
    }

}