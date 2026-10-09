package app.dto;

public record OfferWebhookDTO(
        Long offerId,
        String event
) {
}