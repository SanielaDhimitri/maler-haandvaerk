package app.api;

import app.config.HibernateConfig;
import app.dao.BookingDAO;
import app.dao.ServiceDAO;
import app.controllers.BookingController;
import io.javalin.Javalin;
//forbinder med db
public class ApiServer {
    public static void main(String[] args) {
        var emf = HibernateConfig.getEntityManagerFactory();

        var bookingDAO = new BookingDAO(emf);
        var serviceDAO = new ServiceDAO(emf);
        var controller = new BookingController(bookingDAO, serviceDAO);

        Javalin app = Javalin.create().start(7072);

        app.get("/api/bookings", controller::getAll);
        app.get("/api/bookings/{id}", controller::getById);
        app.post("/api/bookings", controller::create);
        app.put("/api/bookings/{id}", controller::update);
        app.delete("/api/bookings/{id}", controller::delete);
    }
}