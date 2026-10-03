package app.dao;

import app.entities.Booking;
import app.enums.BookingStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class BookingDAO extends GenericDAO<Booking> {

    public BookingDAO(EntityManagerFactory emf) {
        super(emf, Booking.class);
    }

    // Finder booking + services
    @Override
    public Booking findById(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT DISTINCT b FROM Booking b " +
                                    "LEFT JOIN FETCH b.services " +
                                    "WHERE b.id = :id",
                            Booking.class
                    )
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } finally {
            em.close();
        }
    }

    // Finder alle bookinger + services
    @Override
    public List<Booking> findAll() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT DISTINCT b FROM Booking b " +
                            "LEFT JOIN FETCH b.services",
                    Booking.class
            ).getResultList();

        } finally {
            em.close();
        }
    }

    // Specifik metode kun for Booking
    public Booking updateStatus(Long id, BookingStatus nyStatus) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Booking booking = em.find(Booking.class, id);

            if (booking != null) {
                booking.setStatus(nyStatus);
            }

            em.getTransaction().commit();

            return booking;

        } finally {
            em.close();
        }
    }
}