package app.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

// Indeholder de bookingdata, som API'et modtager
public record BookingRequestDTO(
        LocalDate dato,
        LocalTime tid,
        String beskrivelse,
        String kundenavn,
        String email,
        String telefon,
        Set<Long> serviceIds
) {
}