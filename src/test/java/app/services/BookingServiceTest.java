package app.services;

import app.dao.BookingDAO;
import app.enums.BookingStatus;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class BookingServiceTest {

    // =========================
    // BEKRÆFT BOOKING
    // =========================
    @Test
    void bekraeftBooking() {

        BookingDAO bookingDAO = mock(BookingDAO.class);

        BookingService bookingService =
                new BookingService(bookingDAO);

        bookingService.bekraeftBooking(1L);

        verify(bookingDAO).updateStatus(
                1L,
                BookingStatus.GODKENDT
        );
    }


    // =========================
    // AFVIS BOOKING
    // =========================
    @Test
    void afvisBooking() {

        BookingDAO bookingDAO = mock(BookingDAO.class);

        BookingService bookingService =
                new BookingService(bookingDAO);

        bookingService.afvisBooking(1L);

        verify(bookingDAO).updateStatus(
                1L,
                BookingStatus.AFVIST
        );
    }


    // =========================
    // AFSLUT BOOKING
    // =========================
    @Test
    void afslutBooking() {

        BookingDAO bookingDAO = mock(BookingDAO.class);

        BookingService bookingService =
                new BookingService(bookingDAO);

        bookingService.afslutBooking(1L);

        verify(bookingDAO).updateStatus(
                1L,
                BookingStatus.AFSLUTTET
        );
    }
}