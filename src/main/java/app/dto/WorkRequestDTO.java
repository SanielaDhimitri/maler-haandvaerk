package app.dto;

import app.enums.RequestStatus;

import java.util.List;

public record WorkRequestDTO(
        Long id,
        String fornavn,
        String efternavn,
        String email,
        String telefon,
        String adresse,
        String beskrivelse,
        RequestStatus status,
        Long brugerId,
        Long offerId,
        List<WorkRequestDetailDTO> workRequestDetails
) {
}
// WorkRequestDTO → bruges, når vi sender en arbejdsforespørgsel tilbage til klienten