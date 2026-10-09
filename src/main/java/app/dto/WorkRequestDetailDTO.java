package app.dto;

import app.enums.ServiceType;

public record WorkRequestDetailDTO(
        Long id,
        Long serviceId,
        ServiceType serviceType,
        String beskrivelse,
        Double omfang,
        String enhed
) {
}
// WorkRequestDetailDTO → bruges, når vi viser detaljer om en arbejdsforespørgsel