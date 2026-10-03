package app.dao;

import app.config.HibernateTestConfig;
import app.entities.Bruger;
import app.entities.Person;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class PersonDAOTest {

    private static EntityManagerFactory emf;
    private PersonDAO personDAO;

    @BeforeAll
    static void setupAll() {
        emf = HibernateTestConfig.getEntityManagerFactory();
    }

    @BeforeEach
    void setup() {
        personDAO = new PersonDAO(emf);
    }

    @AfterAll
    static void tearDownAll() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void createPerson() {

        Bruger bruger = new Bruger(
                "Test Bruger",
                "test@mail.dk",
                "1234",
                "12345678"
        );

        Person created = personDAO.create(bruger);

        assertNotNull(created.getId());
    }

    @Test
    void findPersonById() {

        Bruger bruger = new Bruger(
                "Anna Test",
                "anna@test.dk",
                "1234",
                "12345678"
        );

        personDAO.create(bruger);

        Person found = personDAO.findById(bruger.getId());

        assertNotNull(found);
        assertEquals("Anna Test", found.getNavn());
    }

    @Test
    void findAll() {

        Bruger bruger1 = new Bruger(
                "Anna",
                "anna@test.dk",
                "1234",
                "11111111"
        );

        Bruger bruger2 = new Bruger(
                "Lars",
                "lars@test.dk",
                "1234",
                "22222222"
        );

        personDAO.create(bruger1);
        personDAO.create(bruger2);

        var personer = personDAO.findAll();

        assertNotNull(personer);
        assertTrue(personer.size() >= 2);
    }

    @Test
    void updatePerson() {

        Bruger bruger = new Bruger(
                "Anna",
                "anna@test.dk",
                "1234",
                "12345678"
        );

        personDAO.create(bruger);

        bruger.setNavn("Anna Jensen");

        personDAO.update(bruger);

        Person updated =
                personDAO.findById(bruger.getId());

        assertEquals("Anna Jensen", updated.getNavn());
    }

    @Test
    void deletePerson() {

        Bruger bruger = new Bruger(
                "Lars",
                "lars@test.dk",
                "1234",
                "12345678"
        );

        personDAO.create(bruger);

        Long id = bruger.getId();

        personDAO.delete(id);

        Person deleted = personDAO.findById(id);

        assertNull(deleted);
    }
}