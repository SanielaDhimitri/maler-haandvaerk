package app.dto;

import app.enums.RequestStatus;

public record WorkRequestDTO(
        Long id,
        String fornavn,
        String efternavn,
        String email,
        String telefon,
        String adresse,
        String beskrivelse,
        RequestStatus status
) {
}