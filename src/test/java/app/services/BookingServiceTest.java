package app.services;//DAO-integrationstests](week5/)


import app.dao.BookingDAO;
import app.enums.BookingStatus;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class BookingServiceTest {

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
}