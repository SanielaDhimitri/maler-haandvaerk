package app.dao;

import app.entities.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ProjectDAO extends GenericDAO<Project> {

    public ProjectDAO(EntityManagerFactory emf) {
        super(emf, Project.class);
    }

    // Finder kun projekter for én bestemt bruger
    public List<Project> findByBrugerId(Long brugerId) {

        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT p FROM Project p " +
                                    "WHERE p.bruger.id = :brugerId",
                            Project.class
                    )
                    .setParameter("brugerId", brugerId)
                    .getResultList();

        } finally {
            em.close();
        }
    }
}