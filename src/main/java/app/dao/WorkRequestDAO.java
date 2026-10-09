package app.dao;

import app.entities.WorkRequest;
import app.enums.RequestStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class WorkRequestDAO extends GenericDAO<WorkRequest> {

    public WorkRequestDAO(EntityManagerFactory emf) {
        super(emf, WorkRequest.class);
    }

    // Finder én arbejdsforespørgsel
    @Override
    public WorkRequest findById(Long id) {

        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT DISTINCT w FROM WorkRequest w " +
                                    "LEFT JOIN FETCH w.workRequestDetails " +
                                    "WHERE w.id = :id",
                            WorkRequest.class
                    )
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } finally {
            em.close();
        }
    }

    // Finder alle arbejdsforespørgsler med details
    @Override
    public List<WorkRequest> findAll() {

        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT DISTINCT w FROM WorkRequest w " +
                            "LEFT JOIN FETCH w.workRequestDetails",
                    WorkRequest.class
            ).getResultList();

        } finally {
            em.close();
        }
    }


    // Specifik metode for WorkRequest
    public WorkRequest updateStatus(Long id, RequestStatus nyStatus) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            WorkRequest workRequest =
                    em.createQuery(
                                    "SELECT DISTINCT w FROM WorkRequest w " +
                                            "LEFT JOIN FETCH w.workRequestDetails " +
                                            "WHERE w.id = :id",
                                    WorkRequest.class
                            )
                            .setParameter("id", id)
                            .getResultStream()
                            .findFirst()
                            .orElse(null);

            if (workRequest != null) {
                workRequest.setStatus(nyStatus);
            }

            em.getTransaction().commit();

            return workRequest;

        } finally {
            em.close();
        }
    }
}