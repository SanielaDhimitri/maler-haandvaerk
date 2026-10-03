package app.dto;

import java.time.LocalDate;

public record OfferRequestDTO(
        Double pris,
        String beskrivelse,
        LocalDate oprettetDato,
        LocalDate gyldigTil,
        Long workRequestId
) {
}