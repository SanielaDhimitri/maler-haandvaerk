package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.controllers.BookingController;
import app.controllers.OfferController;
import app.dao.BookingDAO;
import app.dao.OfferDAO;
import app.dao.ServiceDAO;
import app.dao.WorkRequestDAO;
import app.routes.Routes;
import jakarta.persistence.EntityManagerFactory;
import app.controllers.WorkRequestController;

public class Main {

    public static void main(String[] args) {

        // ENVIRONMENT VARIABLES
        String geoapifyApiKey =
                System.getenv("GEOAPIFY_API_KEY");

        String geminiApiKey =
                System.getenv("GEMINI_API_KEY");


        // CHECK GEOAPIFY API KEY
        if (geoapifyApiKey == null || geoapifyApiKey.isBlank()) {
            System.out.println("Geoapify API key blev ikke fundet!");
        } else {
            System.out.println("Geoapify API key blev fundet!");
        }


        // DATABASE
        EntityManagerFactory emf =
                HibernateConfig.getEntityManagerFactory();



        // DAO
        BookingDAO bookingDAO = new BookingDAO(emf);
        ServiceDAO serviceDAO = new ServiceDAO(emf);
        OfferDAO offerDAO = new OfferDAO(emf);
        WorkRequestDAO workRequestDAO = new WorkRequestDAO(emf);


        // CONTROLLERS
        BookingController bookingController =
                new BookingController(bookingDAO, serviceDAO);

        OfferController offerController =
                new OfferController(offerDAO, workRequestDAO);

        WorkRequestController workRequestController =
                new WorkRequestController(workRequestDAO);


        // ROUTES
        Routes routes =
                new Routes(
                        bookingController,
                        offerController,
                        workRequestController
                );

        // JAVALIN CONFIG
        ApplicationConfig applicationConfig =
                new ApplicationConfig(routes);


        // START SERVER
        applicationConfig.startServer(7072);
    }
}