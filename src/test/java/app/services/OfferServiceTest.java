package app.services;

import app.dao.OfferDAO;
import app.dao.WorkRequestDAO;
import app.entities.Offer;
import app.entities.WorkRequest;
import app.enums.OfferStatus;
import app.enums.RequestStatus;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class OfferServiceTest {

    @Test
    void godkendTilbud() {

        // Mock DAO'er
        OfferDAO offerDAO = mock(OfferDAO.class);
        WorkRequestDAO workRequestDAO = mock(WorkRequestDAO.class);

        // Mock entities
        Offer offer = mock(Offer.class);
        WorkRequest workRequest = mock(WorkRequest.class);

        // Testdata
        when(offerDAO.findById(1L))
                .thenReturn(offer);

        when(offer.getWorkRequest())
                .thenReturn(workRequest);

        when(workRequest.getId())
                .thenReturn(10L);

        // Service
        OfferService offerService =
                new OfferService(offerDAO, workRequestDAO);

        // Kører metoden
        offerService.godkendTilbud(1L);

        // Kontrollerer tilbud
        verify(offerDAO).updateStatus(
                1L,
                OfferStatus.GODKENDT
        );

        // Kontrollerer arbejdsforespørgsel
        verify(workRequestDAO).updateStatus(
                10L,
                RequestStatus.GODKENDT
        );
    }


    @Test
    void afvisTilbud() {

        // Mock DAO'er
        OfferDAO offerDAO = mock(OfferDAO.class);
        WorkRequestDAO workRequestDAO = mock(WorkRequestDAO.class);

        // Mock entities
        Offer offer = mock(Offer.class);
        WorkRequest workRequest = mock(WorkRequest.class);

        // Testdata
        when(offerDAO.findById(1L))
                .thenReturn(offer);

        when(offer.getWorkRequest())
                .thenReturn(workRequest);

        when(workRequest.getId())
                .thenReturn(10L);

        // Service
        OfferService offerService =
                new OfferService(offerDAO, workRequestDAO);

        // Kører metoden
        offerService.afvisTilbud(1L);

        // Kontrollerer tilbud
        verify(offerDAO).updateStatus(
                1L,
                OfferStatus.AFVIST
        );

        // Kontrollerer arbejdsforespørgsel
        verify(workRequestDAO).updateStatus(
                10L,
                RequestStatus.AFVIST
        );
    }
}