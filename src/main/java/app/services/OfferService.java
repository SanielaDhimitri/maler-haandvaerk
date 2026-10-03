package app.services;

import app.dao.OfferDAO;
import app.dao.WorkRequestDAO;
import app.entities.Offer;
import app.enums.OfferStatus;
import app.enums.RequestStatus;

public class OfferService {

    private final OfferDAO offerDAO;
    private final WorkRequestDAO workRequestDAO;

    public OfferService(OfferDAO offerDAO,
                        WorkRequestDAO workRequestDAO) {

        this.offerDAO = offerDAO;
        this.workRequestDAO = workRequestDAO;
    }

    public void godkendTilbud(Long offerId) {

        Offer offer = offerDAO.findById(offerId);

        if (offer != null) {

            offerDAO.updateStatus(
                    offerId,
                    OfferStatus.GODKENDT
            );

            workRequestDAO.updateStatus(
                    offer.getWorkRequest().getId(),
                    RequestStatus.GODKENDT
            );
        }
    }

    public void afvisTilbud(Long offerId) {

        Offer offer = offerDAO.findById(offerId);

        if (offer != null) {

            offerDAO.updateStatus(
                    offerId,
                    OfferStatus.AFVIST
            );

            workRequestDAO.updateStatus(
                    offer.getWorkRequest().getId(),
                    RequestStatus.AFVIST
            );
        }
    }
}