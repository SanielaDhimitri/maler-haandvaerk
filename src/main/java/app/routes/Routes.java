package app.routes;

import app.controllers.BookingController;
import app.controllers.OfferController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import app.controllers.WorkRequestController;

public class Routes {

    private final BookingController bookingController;
    private final OfferController offerController;
    private final WorkRequestController workRequestController;

    public Routes(
            BookingController bookingController,
            OfferController offerController,
            WorkRequestController workRequestController) {

        this.bookingController = bookingController;
        this.offerController = offerController;
        this.workRequestController = workRequestController;
    }

    public EndpointGroup getRoutes() {

        return () -> {

            // BOOKING
            path("/api/bookings", () -> {
                get("/", bookingController::getAll);
                get("/{id}", bookingController::getById);
                post("/", bookingController::create);
                put("/{id}", bookingController::update);
                delete("/{id}", bookingController::delete);
            });

            // OFFER
            path("/api/offers", () -> {
                get("/", offerController::getAll);
                get("/{id}", offerController::getById);
                post("/", offerController::create);
                put("/{id}", offerController::update);
                delete("/{id}", offerController::delete);
            });

            // WORK REQUEST
            path("/api/workrequests", () -> {
                get("/", workRequestController::getAll);
                get("/{id}", workRequestController::getById);
                post("/", workRequestController::create);
                put("/{id}/status", workRequestController::updateStatus);
                delete("/{id}", workRequestController::delete);
            });

        };
    }
}