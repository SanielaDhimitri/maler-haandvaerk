package app.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class GenericDAO<T> implements IDAO<T> {

    protected final EntityManagerFactory emf;
    private final Class<T> entityClass;

    public GenericDAO(EntityManagerFactory emf, Class<T> entityClass) {
        this.emf = emf;
        this.entityClass = entityClass;
    }

    // CREATE
    @Override
    public T create(T entity) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();

            return entity;
        } finally {
            em.close();
        }
    }

    // READ ONE
    @Override
    public T findById(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }

    // READ ALL
    @Override
    public List<T> findAll() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT e FROM " + entityClass.getSimpleName() + " e",
                    entityClass
            ).getResultList();
        } finally {
            em.close();
        }
    }

    // UPDATE
    @Override
    public T update(T entity) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            T updatedEntity = em.merge(entity);

            em.getTransaction().commit();

            return updatedEntity;
        } finally {
            em.close();
        }
    }

    // DELETE
    @Override
    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            T entity = em.find(entityClass, id);

            if (entity != null) {
                em.remove(entity);
            }

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // COUNT
    @Override
    public long count() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT COUNT(e) FROM "
                            + entityClass.getSimpleName()
                            + " e",
                    Long.class
            ).getSingleResult();
        } finally {
            em.close();
        }
    }
}