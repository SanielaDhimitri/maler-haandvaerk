package app.controllers;

import app.dao.OfferDAO;
import app.dao.WorkRequestDAO;
import app.dto.OfferRequestDTO;
import app.entities.Offer;
import app.entities.WorkRequest;
import app.mappers.OfferMapper;
import app.services.EmailService;
import app.services.OfferService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.Map;

public class OfferController {

    private final OfferDAO offerDAO;
    private final WorkRequestDAO workRequestDAO;
    private final OfferService offerService;

    public OfferController(
            OfferDAO offerDAO,
            WorkRequestDAO workRequestDAO,
            OfferService offerService
    ) {
        this.offerDAO = offerDAO;
        this.workRequestDAO = workRequestDAO;
        this.offerService = offerService;
    }


    // =========================
    // GET ALL
    // =========================
    public void getAll(Context ctx) {

        var offers = offerDAO.findAll();

        ctx.status(HttpStatus.OK)
                .json(OfferMapper.toDTOList(offers));
    }


    // =========================
    // GET BY ID
    // =========================
    public void getById(Context ctx) {

        Long id = getId(ctx);

        Offer offer = offerDAO.findById(id);

        if (offer == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of(
                            "message", "Offer not found"
                    ));
            return;
        }

        ctx.status(HttpStatus.OK)
                .json(OfferMapper.toDTO(offer));
    }


    // =========================
    // CREATE
    // Opretter tilbud + sender email
    // =========================
    public void create(Context ctx) {

        OfferRequestDTO request =
                ctx.bodyValidator(OfferRequestDTO.class)

                        .check(
                                o -> o.pris() != null,
                                "Pris skal angives"
                        )

                        .check(
                                o -> o.pris() != null
                                        && o.pris() > 0,
                                "Pris skal være større end 0"
                        )

                        .check(
                                o -> o.beskrivelse() != null
                                        && !o.beskrivelse().isBlank(),
                                "Beskrivelse må ikke være tom"
                        )

                        .check(
                                o -> o.oprettetDato() != null,
                                "Oprettet dato skal angives"
                        )

                        .check(
                                o -> o.gyldigTil() != null,
                                "Gyldig til dato skal angives"
                        )

                        .check(
                                o -> o.oprettetDato() == null
                                        || o.gyldigTil() == null
                                        || !o.gyldigTil()
                                        .isBefore(o.oprettetDato()),
                                "GyldigTil må ikke være før oprettetDato"
                        )

                        .check(
                                o -> o.workRequestId() != null
                                        && o.workRequestId() > 0,
                                "WorkRequest ID skal være positiv"
                        )

                        .get();


        WorkRequest workRequest =
                workRequestDAO.findById(
                        request.workRequestId()
                );

        if (workRequest == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of(
                            "message", "WorkRequest not found"
                    ));
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

        Offer saved =
                offerDAO.findById(offer.getId());


        // =========================
        // SEND TILBUD VIA RESEND
        // =========================

        EmailService emailService = new EmailService();

        emailService.sendTilbud(
                saved.getWorkRequest().getEmail(),
                saved.getId(),
                saved.getPris(),
                saved.getBeskrivelse()
        );


        ctx.status(HttpStatus.CREATED)
                .json(OfferMapper.toDTO(saved));
    }


    // =========================
    // UPDATE
    // =========================
    public void update(Context ctx) {

        Long id = getId(ctx);

        Offer offer = offerDAO.findById(id);

        if (offer == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of(
                            "message", "Offer not found"
                    ));
            return;
        }


        OfferRequestDTO request =
                ctx.bodyValidator(OfferRequestDTO.class)

                        .check(
                                o -> o.pris() != null,
                                "Pris skal angives"
                        )

                        .check(
                                o -> o.pris() != null
                                        && o.pris() > 0,
                                "Pris skal være større end 0"
                        )

                        .check(
                                o -> o.beskrivelse() != null
                                        && !o.beskrivelse().isBlank(),
                                "Beskrivelse må ikke være tom"
                        )

                        .check(
                                o -> o.oprettetDato() != null,
                                "Oprettet dato skal angives"
                        )

                        .check(
                                o -> o.gyldigTil() != null,
                                "Gyldig til dato skal angives"
                        )

                        .check(
                                o -> o.oprettetDato() == null
                                        || o.gyldigTil() == null
                                        || !o.gyldigTil()
                                        .isBefore(o.oprettetDato()),
                                "GyldigTil må ikke være før oprettetDato"
                        )

                        .check(
                                o -> o.workRequestId() != null
                                        && o.workRequestId() > 0,
                                "WorkRequest ID skal være positiv"
                        )

                        .get();


        WorkRequest workRequest =
                workRequestDAO.findById(
                        request.workRequestId()
                );

        if (workRequest == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of(
                            "message", "WorkRequest not found"
                    ));
            return;
        }


        offer.setPris(request.pris());
        offer.setBeskrivelse(request.beskrivelse());
        offer.setOprettetDato(request.oprettetDato());
        offer.setGyldigTil(request.gyldigTil());
        offer.setWorkRequest(workRequest);

        offerDAO.update(offer);

        Offer updated =
                offerDAO.findById(id);

        ctx.status(HttpStatus.OK)
                .json(OfferMapper.toDTO(updated));
    }


    // =========================
    // DELETE
    // =========================
    public void delete(Context ctx) {

        Long id = getId(ctx);

        Offer offer = offerDAO.findById(id);

        if (offer == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of(
                            "message", "Offer not found"
                    ));
            return;
        }

        offerDAO.delete(id);

        ctx.status(HttpStatus.NO_CONTENT);
    }


    // =========================
    // APPROVE VIA REST API
    // =========================
    public void approve(Context ctx) {

        Long id = getId(ctx);

        Offer offer = offerDAO.findById(id);

        if (offer == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of(
                            "message", "Offer not found"
                    ));
            return;
        }

        offerService.godkendTilbud(id);

        Offer updated =
                offerDAO.findById(id);

        ctx.status(HttpStatus.OK)
                .json(OfferMapper.toDTO(updated));
    }


    // =========================
    // REJECT VIA REST API
    // =========================
    public void reject(Context ctx) {

        Long id = getId(ctx);

        Offer offer = offerDAO.findById(id);

        if (offer == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of(
                            "message", "Offer not found"
                    ));
            return;
        }

        offerService.afvisTilbud(id);

        Offer updated =
                offerDAO.findById(id);

        ctx.status(HttpStatus.OK)
                .json(OfferMapper.toDTO(updated));
    }


    // =========================
    // ACCEPT FROM EMAIL
    // Kunden klikker JA
    // =========================
    public void acceptFromEmail(Context ctx) {

        Long id = getId(ctx);

        Offer offer = offerDAO.findById(id);

        if (offer == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .result("Tilbuddet blev ikke fundet.");
            return;
        }


        // Opdater Offer + WorkRequest
        offerService.godkendTilbud(id);


        // Send bekræftelse via Resend
        EmailService emailService =
                new EmailService();

        emailService.sendGodkendelsesmail(
                offer.getWorkRequest().getEmail()
        );


        ctx.status(HttpStatus.OK)
                .result(
                        "Tak! Dit tilbud er blevet godkendt. " +
                                "DH Maler & Byggeservice kontakter dig snarest."
                );
    }


    // =========================
    // DECLINE FROM EMAIL
    // Kunden klikker NEJ
    // =========================
    public void declineFromEmail(Context ctx) {

        Long id = getId(ctx);

        Offer offer = offerDAO.findById(id);

        if (offer == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .result("Tilbuddet blev ikke fundet.");
            return;
        }


        // Opdater Offer + WorkRequest
        offerService.afvisTilbud(id);


        ctx.status(HttpStatus.OK)
                .result(
                        "Tilbuddet er blevet afvist. " +
                                "Tak for din tilbagemelding."
                );
    }


    // =========================
    // VALIDATION AF ID
    // =========================
    private Long getId(Context ctx) {

        return ctx.pathParamAsClass(
                        "id",
                        Long.class
                )
                .check(
                        id -> id > 0,
                        "ID must be positive"
                )
                .get();
    }
}