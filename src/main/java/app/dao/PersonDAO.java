package app.dao;

import app.entities.Person;
import jakarta.persistence.EntityManagerFactory;

public class PersonDAO extends GenericDAO<Person> {

    public PersonDAO(EntityManagerFactory emf) {
        super(emf, Person.class);
    }

    public Person findByEmail(String email) {

        return emf.createEntityManager()
                .createQuery(
                        "SELECT p FROM Person p WHERE p.email = :email",
                        Person.class
                )
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}