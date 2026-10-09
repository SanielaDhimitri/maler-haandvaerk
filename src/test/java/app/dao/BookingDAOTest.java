package app.dao;

import app.config.HibernateTestConfig;
import app.entities.Booking;
import app.entities.Service;
import app.enums.BookingStatus;
import app.enums.ServiceType;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingDAOTest {

    private static EntityManagerFactory emf;

    private BookingDAO bookingDAO;
    private ServiceDAO serviceDAO;

    @BeforeAll
    static void setupAll() {
        emf = HibernateTestConfig.getEntityManagerFactory();
    }

    @BeforeEach
    void setup() {
        bookingDAO = new BookingDAO(emf);
        serviceDAO = new ServiceDAO(emf);
    }

    @AfterAll
    static void tearDownAll() {
        if (emf != null) {
            emf.close();
        }
    }

    // =========================
    // CREATE BOOKING
    // =========================

    @Test
    void createBooking() {

        Service maling = new Service(
                ServiceType.MALING,
                "Test maling"
        );

        serviceDAO.create(maling);

        Booking booking = new Booking(
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 30),
                "Maling af stue",
                "Anna",
                "anna@test.dk",
                "12345678",
                null
        );

        booking.addService(maling);

        Booking created = bookingDAO.create(booking);

        assertNotNull(created.getId());
    }


    // =========================
    // FIND BOOKING BY ID
    // =========================

    @Test
    void findBookingById() {

        Service toomrer = new Service(
                ServiceType.TOOMRER,
                "Test tømrer"
        );

        Service elektriker = new Service(
                ServiceType.ELEKTRIKER,
                "Test elektriker"
        );

        serviceDAO.create(toomrer);
        serviceDAO.create(elektriker);

        Booking booking = new Booking(
                LocalDate.now().plusDays(2),
                LocalTime.of(12, 0),
                "Tømrer og elektrikerarbejde",
                "Lars",
                "lars@test.dk",
                "87654321",
                null
        );

        booking.addService(toomrer);
        booking.addService(elektriker);

        bookingDAO.create(booking);

        Booking found =
                bookingDAO.findById(booking.getId());

        assertNotNull(found);
        assertEquals("Lars", found.getKundenavn());
        assertEquals(2, found.getServices().size());
    }


    // =========================
    // FIND ALL
    // =========================

    @Test
    void findAll() {

        Service vvs = new Service(
                ServiceType.VVS,
                "Test VVS"
        );

        serviceDAO.create(vvs);

        Booking booking = new Booking(
                LocalDate.now().plusDays(3),
                LocalTime.of(14, 0),
                "VVS arbejde",
                "Peter",
                "peter@test.dk",
                "11223344",
                null
        );

        booking.addService(vvs);

        bookingDAO.create(booking);

        var bookings = bookingDAO.findAll();

        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());
    }


    // =========================
    // UPDATE STATUS
    // =========================

    @Test
    void updateStatus() {

        Service murer = new Service(
                ServiceType.MURER,
                "Test murer"
        );

        serviceDAO.create(murer);

        Booking booking = new Booking(
                LocalDate.now().plusDays(4),
                LocalTime.of(15, 0),
                "Murerarbejde",
                "Maria",
                "maria@test.dk",
                "55667788",
                null
        );

        booking.addService(murer);

        bookingDAO.create(booking);

        assertEquals(
                BookingStatus.AFVENTER,
                booking.getStatus()
        );

        bookingDAO.updateStatus(
                booking.getId(),
                BookingStatus.GODKENDT
        );

        Booking updated =
                bookingDAO.findById(booking.getId());

        assertNotNull(updated);

        assertEquals(
                BookingStatus.GODKENDT,
                updated.getStatus()
        );
    }


    // =========================
    // DELETE BOOKING
    // =========================

    @Test
    void deleteBooking() {

        Service reparationer = new Service(
                ServiceType.REPARATIONER,
                "Test reparationer"
        );

        serviceDAO.create(reparationer);

        Booking booking = new Booking(
                LocalDate.now().plusDays(5),
                LocalTime.of(16, 0),
                "Reparation i bolig",
                "Sofie",
                "sofie@test.dk",
                "99887766",
                null
        );

        booking.addService(reparationer);

        bookingDAO.create(booking);

        Long id = booking.getId();

        bookingDAO.delete(id);

        Booking deleted =
                bookingDAO.findById(id);

        assertNull(deleted);
    }


    // =========================
    // UPDATE BOOKING
    // =========================

    @Test
    void updateBooking() {

        Booking booking = new Booking(
                LocalDate.now().plusDays(6),
                LocalTime.of(10, 0),
                "Gammel beskrivelse",
                "Anna",
                "anna@booking.dk",
                "12345678",
                null
        );

        bookingDAO.create(booking);

        // Ny dato skal også være i fremtiden
        LocalDate nyDato =
                LocalDate.now().plusDays(10);

        booking.setDato(nyDato);
        booking.setTid(LocalTime.of(14, 30));
        booking.setBeskrivelse("Ny beskrivelse");

        bookingDAO.update(booking);

        Booking updated =
                bookingDAO.findById(booking.getId());

        assertNotNull(updated);

        assertEquals(
                nyDato,
                updated.getDato()
        );

        assertEquals(
                LocalTime.of(14, 30),
                updated.getTid()
        );

        assertEquals(
                "Ny beskrivelse",
                updated.getBeskrivelse()
        );
    }
}