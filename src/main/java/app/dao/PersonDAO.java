package app.dao;

import app.entities.Person;
import jakarta.persistence.EntityManagerFactory;

public class PersonDAO extends GenericDAO<Person> {

    public PersonDAO(EntityManagerFactory emf) {
        super(emf, Person.class);
    }
}