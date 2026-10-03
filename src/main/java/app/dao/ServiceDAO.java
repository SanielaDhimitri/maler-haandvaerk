package app.dao;

import app.entities.Service;
import app.enums.ServiceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class ServiceDAO extends GenericDAO<Service> {

    public ServiceDAO(EntityManagerFactory emf) {
        super(emf, Service.class);
    }

    // Finder en service ud fra typen
    public Service findByType(ServiceType type) {

        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT s FROM Service s WHERE s.type = :type",
                            Service.class
                    )
                    .setParameter("type", type)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } finally {
            em.close();
        }
    }
}