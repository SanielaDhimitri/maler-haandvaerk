package app.controllers;

import app.dao.BookingDAO;
import app.dao.ServiceDAO;
import app.dto.BookingRequestDTO;
import app.entities.Booking;
import app.entities.Service;
import app.mappers.BookingMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BookingController {

    private final BookingDAO bookingDAO;
    private final ServiceDAO serviceDAO;

    public BookingController(BookingDAO bookingDAO, ServiceDAO serviceDAO) {
        this.bookingDAO = bookingDAO;
        this.serviceDAO = serviceDAO;
    }

    // Henter alle bookinger
    public void getAll(Context ctx) {

        var bookings = bookingDAO.findAll();

        ctx.status(HttpStatus.OK)
                .json(BookingMapper.toDTOList(bookings));
    }

    // Henter én booking ud fra ID
    public void getById(Context ctx) {

        Long id = getId(ctx);

        Booking booking = bookingDAO.findById(id);

        if (booking == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of("message", "Booking not found"));
            return;
        }

        ctx.status(HttpStatus.OK)
                .json(BookingMapper.toDTO(booking));
    }

    // Opretter en booking
    public void create(Context ctx) {

        BookingRequestDTO request =
                ctx.bodyAsClass(BookingRequestDTO.class);

        Set<Service> services =
                findServices(request.serviceIds());

        if (services == null) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json(Map.of("message", "Service not found"));
            return;
        }

        Booking booking = new Booking(
                request.dato(),
                request.tid(),
                request.beskrivelse(),
                request.kundenavn(),
                request.email(),
                request.telefon(),
                null
        );

        booking.setServices(services);

        bookingDAO.create(booking);

        Booking saved =
                bookingDAO.findById(booking.getId());

        ctx.status(HttpStatus.CREATED)
                .json(BookingMapper.toDTO(saved));
    }

    // Opdaterer en booking
    public void update(Context ctx) {

        Long id = getId(ctx);

        Booking booking =
                bookingDAO.findById(id);

        if (booking == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of("message", "Booking not found"));
            return;
        }

        BookingRequestDTO request =
                ctx.bodyAsClass(BookingRequestDTO.class);

        Set<Service> services =
                findServices(request.serviceIds());

        if (services == null) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json(Map.of("message", "Service not found"));
            return;
        }

        booking.setDato(request.dato());
        booking.setTid(request.tid());
        booking.setBeskrivelse(request.beskrivelse());
        booking.setKundenavn(request.kundenavn());
        booking.setEmail(request.email());
        booking.setTelefon(request.telefon());
        booking.setServices(services);

        bookingDAO.update(booking);

        Booking updated =
                bookingDAO.findById(id);

        ctx.status(HttpStatus.OK)
                .json(BookingMapper.toDTO(updated));
    }

    // Sletter en booking
    public void delete(Context ctx) {

        Long id = getId(ctx);

        Booking booking =
                bookingDAO.findById(id);

        if (booking == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of("message", "Booking not found"));
            return;
        }

        bookingDAO.delete(id);

        ctx.status(HttpStatus.NO_CONTENT)
                .json(Map.of("message", "Booking deleted successfully"));
    }

    // Læser ID fra URL
    private Long getId(Context ctx) {

        return ctx.pathParamAsClass("id", Long.class)
                .check(id -> id > 0, "ID must be positive")
                .get();
    }

    // Finder services ud fra deres ID'er
    private Set<Service> findServices(Set<Long> serviceIds) {

        Set<Service> services = new HashSet<>();

        if (serviceIds == null) {
            return services;
        }

        for (Long serviceId : serviceIds) {

            Service service =
                    serviceDAO.findById(serviceId);

            if (service == null) {
                return null;
            }

            services.add(service);
        }

        return services;
    }
}