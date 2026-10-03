package app.config;

import jakarta.persistence.EntityManagerFactory;

import java.util.Properties;

public class HibernateTestConfig {

    public static EntityManagerFactory getEntityManagerFactory() {

        Properties props = HibernateBaseProperties.createBase();

        props.put(
                "hibernate.connection.driver_class",
                "org.testcontainers.jdbc.ContainerDatabaseDriver"
        );

        props.put(
                "hibernate.connection.url",
                "jdbc:tc:postgresql:16:///test_db"
        );

        props.put(
                "hibernate.connection.username",
                "test"
        );

        props.put(
                "hibernate.connection.password",
                "test"
        );

        props.put(
                "hibernate.hbm2ddl.auto",
                "create-drop"
        );

        return HibernateEmfBuilder.build(props);
    }
}