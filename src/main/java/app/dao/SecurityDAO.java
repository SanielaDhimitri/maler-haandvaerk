package app.dao;

import app.entities.Bruger;
import app.entities.Person;
import app.entities.Role;
import app.security.ISecurityDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class SecurityDAO implements ISecurityDAO {

    private final EntityManagerFactory emf;

    public SecurityDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Person getVerifiedUser(String email, String password) {

        EntityManager em = emf.createEntityManager();

        try {
            Person person = em.createQuery(
                            "SELECT p FROM Person p WHERE p.email = :email",
                            Person.class
                    )
                    .setParameter("email", email)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (person != null && person.verifyPassword(password)) {
                return person;
            }

            return null;

        } finally {
            em.close();
        }
    }

    @Override
    public Person createUser(
            String navn,
            String email,
            String password,
            String telefon
    ) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Bruger bruger = new Bruger(
                    navn,
                    email,
                    password,
                    telefon
            );

            em.persist(bruger);

            em.getTransaction().commit();

            return bruger;

        } finally {
            em.close();
        }
    }


    @Override
    public Role createRole(String role) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Role newRole = new Role(role);
            em.persist(newRole);

            em.getTransaction().commit();

            return newRole;

        } finally {
            em.close();
        }
    }

    @Override
    public Person addUserRole(String email, String role) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Person person = em.createQuery(
                            "SELECT p FROM Person p WHERE p.email = :email",
                            Person.class
                    )
                    .setParameter("email", email)
                    .getSingleResult();

            Role foundRole = em.find(Role.class, role);

            person.addRole(foundRole);

            em.getTransaction().commit();

            return person;

        } finally {
            em.close();
        }
    }
    public Person findByEmail(String email) {

        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT p FROM Person p WHERE p.email = :email",
                            Person.class
                    )
                    .setParameter("email", email)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } finally {
            em.close();
        }
    }
}