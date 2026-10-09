package app.controllers;

import app.dao.OfferDAO;
import app.dto.OfferWebhookDTO;
import app.entities.Offer;
import app.services.OfferService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.Map;

public class WebhookController {

    private final OfferDAO offerDAO;
    private final OfferService offerService;

    public WebhookController(
            OfferDAO offerDAO,
            OfferService offerService
    ) {
        this.offerDAO = offerDAO;
        this.offerService = offerService;
    }

    public void handleOfferWebhook(Context ctx) {

        OfferWebhookDTO webhook =
                ctx.bodyAsClass(OfferWebhookDTO.class);

        if (webhook.offerId() == null || webhook.event() == null) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json(Map.of(
                            "message", "offerId og event skal angives"
                    ));
            return;
        }

        Offer offer =
                offerDAO.findById(webhook.offerId());

        if (offer == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of(
                            "message", "Offer not found"
                    ));
            return;
        }

        switch (webhook.event().toUpperCase()) {

            case "ACCEPTED" ->
                    offerService.godkendTilbud(
                            webhook.offerId()
                    );

            case "REJECTED" ->
                    offerService.afvisTilbud(
                            webhook.offerId()
                    );

            default -> {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json(Map.of(
                                "message", "Unknown webhook event"
                        ));
                return;
            }
        }

        ctx.status(HttpStatus.OK)
                .json(Map.of(
                        "message", "Webhook received",
                        "offerId", webhook.offerId(),
                        "event", webhook.event()
                ));
    }
}