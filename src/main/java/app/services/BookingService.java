package app.services;

import app.dao.BookingDAO;
import app.enums.BookingStatus;

public class BookingService {

    private final BookingDAO bookingDAO;

    public BookingService(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    public void bekraeftBooking(Long bookingId) {
        bookingDAO.updateStatus(
                bookingId,
                BookingStatus.GODKENDT
        );
    }

    public void afvisBooking(Long bookingId) {
        bookingDAO.updateStatus(
                bookingId,
                BookingStatus.AFVIST
        );
    }

    public void afslutBooking(Long bookingId) {
        bookingDAO.updateStatus(
                bookingId,
                BookingStatus.AFSLUTTET
        );
    }
}