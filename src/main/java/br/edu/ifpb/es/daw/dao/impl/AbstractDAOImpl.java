package br.edu.ifpb.es.daw.dao.impl;

import br.edu.ifpb.es.daw.dao.DAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class AbstractDAOImpl<T> implements DAO<T> {

	protected EntityManager entityManager;
	protected Class<T> entityClass;

	public AbstractDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
		// Obtém a classe genérica da entidade em tempo de execução
		@SuppressWarnings("unchecked")
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}

	@Override
	public void save(T entity) {
		entityManager.getTransaction().begin();
		try {
			entityManager.persist(entity);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			if (entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().rollback();
			}
			throw new RuntimeException("Erro ao salvar a entidade: " + entityClass.getSimpleName(), e);
		}
	}

	@Override
	public T findById(Long id) {
		return entityManager.find(entityClass, id);
	}

	@Override
	public List<T> findAll() {
		String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
		TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
		return query.getResultList();
	}

	@Override
	public void update(T entity) {
		entityManager.getTransaction().begin();
		try {
			entityManager.merge(entity);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			if (entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().rollback();
			}
			throw new RuntimeException("Erro ao atualizar a entidade: " + entityClass.getSimpleName(), e);
		}
	}

	@Override
	public void delete(T entity) {
		entityManager.getTransaction().begin();
		try {
			entityManager.remove(entityManager.merge(entity));
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			if (entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().rollback();
			}
			throw new RuntimeException("Erro ao remover a entidade: " + entityClass.getSimpleName(), e);
		}
	}

	@Override
	public void deleteAll() {
		entityManager.getTransaction().begin();
		try {
			String jpql = "DELETE FROM " + entityClass.getSimpleName();
			entityManager.createQuery(jpql).executeUpdate();
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			if (entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().rollback();
			}
			throw new RuntimeException("Erro ao remover todas as entidades: " + entityClass.getSimpleName(), e);
		}
	}
}