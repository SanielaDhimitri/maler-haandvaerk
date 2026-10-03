package app.dao;

import app.config.HibernateTestConfig;
import app.entities.Service;
import app.enums.ServiceType;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ServiceDAOTest {

    private static EntityManagerFactory emf;
    private ServiceDAO serviceDAO;

    @BeforeAll
    static void setupAll() {
        emf = HibernateTestConfig.getEntityManagerFactory();
    }

    @BeforeEach
    void setup() {
        serviceDAO = new ServiceDAO(emf);
    }

    @AfterAll
    static void tearDownAll() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void createService() {

        Service service = new Service(
                ServiceType.REPARATIONER,
                "Test reparationer"
        );

        Service created = serviceDAO.create(service);

        assertNotNull(created.getId());
    }

    @Test
    void findServiceById() {

        Service service = new Service(
                ServiceType.ELEKTRIKER,
                "Test elektriker"
        );

        serviceDAO.create(service);

        Service found =
                serviceDAO.findById(service.getId());

        assertNotNull(found);
        assertEquals(ServiceType.ELEKTRIKER, found.getType());
        assertEquals("Test elektriker", found.getBeskrivelse());
    }

    @Test
    void findAll() {

        Service maling = new Service(
                ServiceType.MALING,
                "Test maling"
        );

        Service vvs = new Service(
                ServiceType.VVS,
                "Test VVS"
        );

        serviceDAO.create(maling);
        serviceDAO.create(vvs);

        var services = serviceDAO.findAll();

        assertNotNull(services);
        assertTrue(services.size() >= 2);
    }

    @Test
    void findByType() {

        Service service = new Service(
                ServiceType.MURER,
                "Test murerarbejde"
        );

        serviceDAO.create(service);

        Service found =
                serviceDAO.findByType(ServiceType.MURER);

        assertNotNull(found);
        assertEquals(ServiceType.MURER, found.getType());
        assertEquals("Test murerarbejde", found.getBeskrivelse());
    }
}