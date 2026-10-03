package app.controllers;

import app.dao.OfferDAO;
import app.dao.WorkRequestDAO;
import app.dto.OfferRequestDTO;
import app.entities.Offer;
import app.entities.WorkRequest;
import app.mappers.OfferMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.Map;

public class OfferController {

    private final OfferDAO offerDAO;
    private final WorkRequestDAO workRequestDAO;

    public OfferController(OfferDAO offerDAO, WorkRequestDAO workRequestDAO) {
        this.offerDAO = offerDAO;
        this.workRequestDAO = workRequestDAO;
    }

    // Henter alle tilbud
    public void getAll(Context ctx) {

        var offers = offerDAO.findAll();

        ctx.status(HttpStatus.OK)
                .json(OfferMapper.toDTOList(offers));
    }

    // Henter ét tilbud ud fra ID
    public void getById(Context ctx) {

        Long id = getId(ctx);

        Offer offer = offerDAO.findById(id);

        if (offer == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of("message", "Offer not found"));
            return;
        }

        ctx.status(HttpStatus.OK)
                .json(OfferMapper.toDTO(offer));
    }

    // Opretter et tilbud
    public void create(Context ctx) {

        OfferRequestDTO request =
                ctx.bodyAsClass(OfferRequestDTO.class);

        WorkRequest workRequest =
                workRequestDAO.findById(request.workRequestId());

        if (workRequest == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of("message", "WorkRequest not found"));
            return;
        }

        Offer offer = new Offer(
                request.pris(),
                request.beskrivelse(),
                request.oprettetDato(),
                request.gyldigTil(),
                workRequest
        );

        offerDAO.create(offer);

        Offer saved = offerDAO.findById(offer.getId());

        ctx.status(HttpStatus.CREATED)
                .json(OfferMapper.toDTO(saved));
    }

    // Opdaterer et eksisterende tilbud
    public void update(Context ctx) {

        Long id = getId(ctx);

        Offer offer = offerDAO.findById(id);

        if (offer == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of("message", "Offer not found"));
            return;
        }

        OfferRequestDTO request =
                ctx.bodyAsClass(OfferRequestDTO.class);

        WorkRequest workRequest =
                workRequestDAO.findById(request.workRequestId());

        if (workRequest == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of("message", "WorkRequest not found"));
            return;
        }

        offer.setPris(request.pris());
        offer.setBeskrivelse(request.beskrivelse());
        offer.setOprettetDato(request.oprettetDato());
        offer.setGyldigTil(request.gyldigTil());
        offer.setWorkRequest(workRequest);

        offerDAO.update(offer);

        Offer updated = offerDAO.findById(id);

        ctx.status(HttpStatus.OK)
                .json(OfferMapper.toDTO(updated));
    }

    // Sletter et tilbud
    public void delete(Context ctx) {

        Long id = getId(ctx);

        Offer offer = offerDAO.findById(id);

        if (offer == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of("message", "Offer not found"));
            return;
        }

        offerDAO.delete(id);

        ctx.status(HttpStatus.NO_CONTENT)
                .json(Map.of("message", "Offer deleted successfully"));
    }

    // Læser ID fra URL
    private Long getId(Context ctx) {

        return ctx.pathParamAsClass("id", Long.class)
                .check(id -> id > 0, "ID must be positive")
                .get();
    }
}