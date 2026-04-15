package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.DAO;
import jakarta.persistence.*;
import java.util.List;

public abstract class AbstractDAOImpl<T> implements DAO<T> {

	private static EntityManagerFactory emf;
	protected final Class<T> entityClass;

	protected AbstractDAOImpl(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public static void initialize(String persistenceUnitName) {
		if (emf == null) {
			emf = Persistence.createEntityManagerFactory(persistenceUnitName);
		}
	}

	protected EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

	@Override
	public void save(T entity) {
		try (EntityManager em = getEntityManager()) {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			try {
				em.persist(entity);
				tx.commit();
			} catch (Exception e) {
				if (tx.isActive()) tx.rollback();
				throw e;
			}
		}
	}

	@Override
	public T findById(Long id) {
		try (EntityManager em = getEntityManager()) {
			return em.find(entityClass, id);
		}
	}

	@Override
	public List<T> findAll() {
		try (EntityManager em = getEntityManager()) {
			String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
			TypedQuery<T> query = em.createQuery(jpql, entityClass);
			return query.getResultList();
		}
	}

	@Override
	public void update(T entity) {
		try (EntityManager em = getEntityManager()) {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			try {
				em.merge(entity);
				tx.commit();
			} catch (Exception e) {
				if (tx.isActive()) tx.rollback();
				throw e;
			}
		}
	}

	@Override
	public void delete(T entity) {
		try (EntityManager em = getEntityManager()) {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			try {
				T managedEntity = em.merge(entity);
				em.remove(managedEntity);
				tx.commit();
			} catch (Exception e) {
				if (tx.isActive()) tx.rollback();
				throw e;
			}
		}
	}

	@Override
	public void deleteAll() {
		try (EntityManager em = getEntityManager()) {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			try {
				String jpql = "DELETE FROM " + entityClass.getSimpleName();
				Query query = em.createQuery(jpql);
				query.executeUpdate();
				tx.commit();
			} catch (Exception e) {
				if (tx.isActive()) tx.rollback();
				throw e;
			}
		}
	}

    public static void closeFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}