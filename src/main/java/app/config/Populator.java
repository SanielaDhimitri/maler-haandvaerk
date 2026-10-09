package app.config;

import app.dao.PersonDAO;
import app.entities.Bruger;
import jakarta.persistence.EntityManagerFactory;

public class Populator {

    public static void populate(EntityManagerFactory emf) {

        PersonDAO personDAO = new PersonDAO(emf);

        // Opret Ela vetëm nëse nuk ekziston
        if (personDAO.findByEmail("ela@test.dk") == null) {

            Bruger bruger1 = new Bruger(
                    "ela",
                    "ela@test.dk",
                    "1234",
                    "12345678"
            );

            personDAO.create(bruger1);

            System.out.println("Ela blev oprettet.");
        }

        // Opret Anna vetëm nëse nuk ekziston
        if (personDAO.findByEmail("anna@test.dk") == null) {

            Bruger bruger2 = new Bruger(
                    "Anna Jensen",
                    "anna@test.dk",
                    "1234",
                    "87654321"
            );

            personDAO.create(bruger2);

            System.out.println("Anna blev oprettet.");
        }
    }
}