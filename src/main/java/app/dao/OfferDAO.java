package app.dao;

import app.entities.Offer;
import app.entities.WorkRequest;
import app.enums.OfferStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class OfferDAO extends GenericDAO<Offer> {

    public OfferDAO(EntityManagerFactory emf) {
        super(emf, Offer.class);
    }

    public Offer updateStatus(Long id, OfferStatus nyStatus) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Offer offer = em.find(Offer.class, id);

            if (offer != null) {
                offer.setStatus(nyStatus);
            }

            em.getTransaction().commit();

            return offer;

        } finally {
            em.close();
        }
    }
    @Override
    public void delete(Long id) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Offer offer = em.find(Offer.class, id);

            if (offer != null) {

                WorkRequest workRequest = offer.getWorkRequest();

                if (workRequest != null) {
                    workRequest.setOffer(null);
                }

                em.remove(offer);
            }

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }
}