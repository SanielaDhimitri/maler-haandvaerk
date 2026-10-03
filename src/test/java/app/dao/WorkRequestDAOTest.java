package app.dao;

import app.config.HibernateTestConfig;
import app.entities.WorkRequest;
import app.enums.RequestStatus;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class WorkRequestDAOTest {

    private static EntityManagerFactory emf;
    private WorkRequestDAO workRequestDAO;

    @BeforeAll
    static void setupAll() {
        emf = HibernateTestConfig.getEntityManagerFactory();
    }

    @BeforeEach
    void setup() {
        workRequestDAO = new WorkRequestDAO(emf);
    }

    @AfterAll
    static void tearDownAll() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void createWorkRequest() {

        WorkRequest workRequest = new WorkRequest(
                "Lars",
                "Hansen",
                "lars@test.dk",
                "12345678",
                "Gentofte",
                "Maling af lejlighed",
                null
        );

        WorkRequest created =
                workRequestDAO.create(workRequest);

        assertNotNull(created.getId());
    }

    @Test
    void findWorkRequestById() {

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

        WorkRequest found =
                workRequestDAO.findById(workRequest.getId());

        assertNotNull(found);
        assertEquals("Anna", found.getFornavn());
        assertEquals("Jensen", found.getEfternavn());
        assertEquals(
                "Renovering af køkken",
                found.getBeskrivelse()
        );
    }

    @Test
    void getAllWorkRequests() {

        WorkRequest workRequest = new WorkRequest(
                "Peter",
                "Nielsen",
                "peter@test.dk",
                "11223344",
                "Gentofte",
                "Renovering af badeværelse",
                null
        );

        workRequestDAO.create(workRequest);

        var workRequests =
                workRequestDAO.findAll();

        assertNotNull(workRequests);
        assertFalse(workRequests.isEmpty());
    }

    @Test
    void updateStatus() {

        WorkRequest workRequest = new WorkRequest(
                "Maria",
                "Hansen",
                "maria@test.dk",
                "55667788",
                "Gentofte",
                "Maling af hus",
                null
        );

        workRequestDAO.create(workRequest);

        // Kontroller startstatus
        assertEquals(
                RequestStatus.NY,
                workRequest.getStatus()
        );

        // Opdater status
        workRequestDAO.updateStatus(
                workRequest.getId(),
                RequestStatus.UNDER_BEHANDLING
        );

        // Hent igen fra databasen
        WorkRequest updated =
                workRequestDAO.findById(workRequest.getId());

        assertEquals(
                RequestStatus.UNDER_BEHANDLING,
                updated.getStatus()
        );
    }
}