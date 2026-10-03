package app.dto;

import app.enums.OfferStatus;

import java.time.LocalDate;

public record OfferDTO(
        Long id,
        Double pris,
        String beskrivelse,
        LocalDate oprettetDato,
        LocalDate gyldigTil,
        OfferStatus status,
        Long workRequestId
) {
}