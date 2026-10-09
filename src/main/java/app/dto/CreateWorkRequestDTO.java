package app.dto;

public record CreateWorkRequestDTO(
        String fornavn,
        String efternavn,
        String email,
        String telefon,
        String adresse,
        String beskrivelse
) {
}

// CreateWorkRequestDTO → bruges, når klienten opretter en arbejdsforespørgsel