package br.edu.ifpb.es.daw.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("daw");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}