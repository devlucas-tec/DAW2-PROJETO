package br.edu.ifpb.es.daw.dao;

import java.util.List;


public interface DAO<T> {

    void save(T entity);

    T findById(Long id);

    List<T> findAll();

    void update(T entity);

    void delete(T entity);

    void deleteAll();
}