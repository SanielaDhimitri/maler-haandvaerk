package app.routes;

import app.controllers.BookingController;
import app.controllers.ChatController;
import app.controllers.OfferController;
import app.controllers.WorkRequestController;
import io.javalin.config.JavalinConfig;
import app.controllers.WebhookController;
import app.controllers.AuthController;

public class Routes {

    private final BookingController bookingController;
    private final OfferController offerController;
    private final WorkRequestController workRequestController;
    private final ChatController chatController;
    private final WebhookController webhookController;
    private final AuthController authController;

    public Routes(
            BookingController bookingController,
            OfferController offerController,
            WorkRequestController workRequestController,
            ChatController chatController,
            WebhookController webhookController,
            AuthController authController
    ) {
        this.bookingController = bookingController;
        this.offerController = offerController;
        this.workRequestController = workRequestController;
        this.chatController = chatController;
        this.webhookController = webhookController;
        this.authController = authController;
    }


    public void register(JavalinConfig config) {

        // =========================
        // BOOKING ROUTES
        // =========================

        config.routes.get("/api/bookings", bookingController::getAll);

        config.routes.get("/api/bookings/{id}", bookingController::getById);

        config.routes.post("/api/bookings", bookingController::create);

        config.routes.put("/api/bookings/{id}", bookingController::update);

        config.routes.delete("/api/bookings/{id}", bookingController::delete);


        // =========================
        // OFFER ROUTES
        // =========================

        config.routes.get("/api/offers", offerController::getAll);

        config.routes.get("/api/offers/{id}", offerController::getById);

        config.routes.post("/api/offers", offerController::create);

        config.routes.put("/api/offers/{id}", offerController::update);

        config.routes.delete("/api/offers/{id}", offerController::delete);

        config.routes.put("/api/offers/{id}/approve", offerController::approve);

        config.routes.put("/api/offers/{id}/reject", offerController::reject);
// Kunden svarer på tilbud via email
        config.routes.get("/api/offers/{id}/accept", offerController::acceptFromEmail);

        config.routes.get("/api/offers/{id}/decline", offerController::declineFromEmail);

        // =========================
        // WORK REQUEST ROUTES
        // =========================

        config.routes.get("/api/workrequests", workRequestController::getAll);

        config.routes.get("/api/workrequests/{id}", workRequestController::getById);

        config.routes.post("/api/workrequests", workRequestController::create);

        config.routes.put("/api/workrequests/{id}/status", workRequestController::updateStatus);

        config.routes.delete("/api/workrequests/{id}", workRequestController::delete);


        // =========================
        // GEMINI CHAT ROUTE
        // =========================

        config.routes.post("/api/chat", chatController::ask);
        // =========================
// WEBHOOK ROUTE
// =========================

        config.routes.post("/api/webhooks/offers", webhookController::handleOfferWebhook);


        // =========================
// AUTH ROUTES
// =========================

        config.routes.post("/api/auth/register", authController::register);
        config.routes.post("/api/auth/login", authController::login);
}
}