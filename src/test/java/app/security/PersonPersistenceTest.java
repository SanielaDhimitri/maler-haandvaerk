package app.security;
//testes pasword=hash me byscrypt
import app.config.HibernateTestConfig;
import app.entities.Bruger;
import app.entities.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class PersonPersistenceTest {

    private static EntityManagerFactory emf;

    @BeforeAll
    static void setupAll() {
        emf = HibernateTestConfig.getEntityManagerFactory();
    }

    @AfterAll
    static void tearDownAll() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void brugerAndRoleArePersistedWithHashedPassword() {

        // GIVEN
        Role brugerRole = new Role("BRUGER");

        Bruger anna = new Bruger(
                "Anna",
                "anna@test.dk",
                "anna123",
                "12345678"
        );

        anna.addRole(brugerRole);

        Long brugerId;

        // WHEN - save
        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();

            em.persist(brugerRole);
            em.persist(anna);

            em.getTransaction().commit();

            brugerId = anna.getId();
        }

        // THEN - reload from database
        try (EntityManager em = emf.createEntityManager()) {

            Bruger reloaded =
                    em.find(Bruger.class, brugerId);

            assertThat(reloaded, notNullValue());

            assertThat(
                    reloaded.getNavn(),
                    equalTo("Anna")
            );

            assertThat(
                    reloaded.getRolesAsStrings(),
                    hasItem("BRUGER")
            );

            assertThat(
                    reloaded.getPasswordHash(),
                    not(equalTo("anna123"))
            );

            assertThat(
                    reloaded.verifyPassword("anna123"),
                    is(true)
            );

            assertThat(
                    reloaded.verifyPassword("wrongpassword"),
                    is(false)
            );
        }
    }
}