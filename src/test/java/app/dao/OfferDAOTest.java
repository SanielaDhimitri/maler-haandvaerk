package app.dao;

import app.config.HibernateTestConfig;
import app.entities.Offer;
import app.entities.WorkRequest;
import app.enums.OfferStatus;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OfferDAOTest {

    private static EntityManagerFactory emf;

    private OfferDAO offerDAO;
    private WorkRequestDAO workRequestDAO;

    @BeforeAll
    static void setupAll() {
        emf = HibernateTestConfig.getEntityManagerFactory();
    }

    @BeforeEach
    void setup() {
        offerDAO = new OfferDAO(emf);
        workRequestDAO = new WorkRequestDAO(emf);
    }

    @AfterAll
    static void tearDownAll() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void createOffer() {

        WorkRequest workRequest = new WorkRequest(
                "Lars",
                "Hansen",
                "lars@test.dk",
                "12345678",
                "Gentofte",
                "Maling af lejlighed",
                null
        );

        workRequestDAO.create(workRequest);

        Offer offer = new Offer(
                15000.0,
                "Tilbud på maling af lejlighed",
                LocalDate.of(2026, 9, 5),
                LocalDate.of(2026, 9, 30),
                workRequest
        );

        Offer created = offerDAO.create(offer);

        assertNotNull(created.getId());
    }

    @Test
    void findOfferById() {

        WorkRequest workRequest = new WorkRequest(
                "Anna",
                "Jensen",
                "anna@test.dk",
                "87654321",
                "Lyngby",
                "Renovering af køkken",
                null
        );

        workRequestDAO.create(workRequest);

        Offer offer = new Offer(
                25000.0,
                "Tilbud på renovering af køkken",
                LocalDate.of(2026, 9, 5),
                LocalDate.of(2026, 10, 5),
                workRequest
        );

        offerDAO.create(offer);

        Offer found =
                offerDAO.findById(offer.getId());

        assertNotNull(found);
        assertEquals(25000.0, found.getPris());
        assertEquals(
                "Tilbud på renovering af køkken",
                found.getBeskrivelse()
        );
    }

    @Test
    void findAll() {

        WorkRequest workRequest = new WorkRequest(
                "Peter",
                "Nielsen",
                "peter@test.dk",
                "11223344",
                "Gentofte",
                "Maling af hus",
                null
        );

        workRequestDAO.create(workRequest);

        Offer offer = new Offer(
                18000.0,
                "Tilbud på maling af hus",
                LocalDate.of(2026, 9, 5),
                LocalDate.of(2026, 10, 5),
                workRequest
        );

        offerDAO.create(offer);

        var offers = offerDAO.findAll();

        assertNotNull(offers);
        assertFalse(offers.isEmpty());
    }

    @Test
    void updateStatus() {

        WorkRequest workRequest = new WorkRequest(
                "Maria",
                "Hansen",
                "maria@test.dk",
                "55667788",
                "Gentofte",
                "Renovering af badeværelse",
                null
        );

        workRequestDAO.create(workRequest);

        Offer offer = new Offer(
                30000.0,
                "Tilbud på renovering af badeværelse",
                LocalDate.of(2026, 9, 5),
                LocalDate.of(2026, 10, 5),
                workRequest
        );

        offerDAO.create(offer);

        // Status starter som AFVENTER
        assertEquals(
                OfferStatus.AFVENTER,
                offer.getStatus()
        );

        // Ændr status
        offerDAO.updateStatus(
                offer.getId(),
                OfferStatus.GODKENDT
        );

        // Hent tilbuddet igen fra databasen
        Offer updated =
                offerDAO.findById(offer.getId());

        assertEquals(
                OfferStatus.GODKENDT,
                updated.getStatus()
        );
    }
}