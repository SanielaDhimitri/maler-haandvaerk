package app;

import app.api.GeminiApi;
import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.controllers.BookingController;
import app.controllers.ChatController;
import app.controllers.OfferController;
import app.controllers.WorkRequestController;
import app.dao.BookingDAO;
import app.dao.OfferDAO;
import app.dao.ServiceDAO;
import app.dao.WorkRequestDAO;
import app.routes.Routes;
import app.services.ChatService;
import app.services.OfferService;
import jakarta.persistence.EntityManagerFactory;
import app.controllers.WebhookController;
import app.config.Populator;
import app.controllers.AuthController;
import app.dao.SecurityDAO;

public class Main {

    public static void main(String[] args) {

        // =========================
        // ENVIRONMENT VARIABLES
        // =========================

        String geoapifyApiKey =
                System.getenv("GEOAPIFY_API_KEY");

        String geminiApiKey =
                System.getenv("GEMINI_API_KEY");

        String resendApiKey =
                System.getenv("RESEND_API_KEY");



        // =========================
        // CHECK GEOAPIFY API KEY
        // =========================

        if (geoapifyApiKey == null || geoapifyApiKey.isBlank()) {
            System.out.println("Geoapify API key blev ikke fundet!");
        } else {
            System.out.println("Geoapify API key blev fundet!");
        }


        // =========================
        // CHECK GEMINI API KEY
        // =========================

        if (geminiApiKey == null || geminiApiKey.isBlank()) {
            System.out.println("Gemini API key blev ikke fundet!");
        } else {
            System.out.println("Gemini API key blev fundet!");
        }

// =========================
// CHECK RESEND API KEY
// =========================

        if (resendApiKey == null || resendApiKey.isBlank()) {
            System.out.println("Resend API key blev ikke fundet!");
        } else {
            System.out.println("Resend API key blev fundet!");
        }
        // =========================
        // DATABASE
        // =========================

        EntityManagerFactory emf =
                HibernateConfig.getEntityManagerFactory();

        Populator.populate(emf);
        // =========================
        // DAO
        // =========================

        BookingDAO bookingDAO =
                new BookingDAO(emf);

        ServiceDAO serviceDAO =
                new ServiceDAO(emf);

        OfferDAO offerDAO =
                new OfferDAO(emf);

        WorkRequestDAO workRequestDAO =
                new WorkRequestDAO(emf);
        SecurityDAO securityDAO = new SecurityDAO(emf);
        // =========================
        // SERVICES
        // =========================

        OfferService offerService =
                new OfferService(
                        offerDAO,
                        workRequestDAO
                );


        GeminiApi geminiApi =
                new GeminiApi(geminiApiKey);

        ChatService chatService =
                new ChatService(geminiApi);


        // =========================
        // CONTROLLERS
        // =========================

        BookingController bookingController =
                new BookingController(
                        bookingDAO,
                        serviceDAO
                );


        OfferController offerController =
                new OfferController(
                        offerDAO,
                        workRequestDAO,
                        offerService
                );


        WorkRequestController workRequestController =
                new WorkRequestController(
                        workRequestDAO
                );


        ChatController chatController =
                new ChatController(
                        chatService
                );


        WebhookController webhookController =
                new WebhookController(
                        offerDAO,
                        offerService
                );
        AuthController authController =
                new AuthController(securityDAO);
// =========================
// ROUTES
// =========================

        Routes routes =
                new Routes(
                        bookingController,
                        offerController,
                        workRequestController,
                        chatController,
                        webhookController,
                        authController
                );


        // =========================
        // JAVALIN CONFIG
        // =========================

        ApplicationConfig applicationConfig =
                new ApplicationConfig(routes);


        // =========================
        // START SERVER
        // =========================

        applicationConfig.startServer(7072);
    }
}