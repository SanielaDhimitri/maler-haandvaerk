package app.dto;

import app.enums.BookingStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public record BookingDTO(
        Long id,
        LocalDate dato,
        LocalTime tid,
        String beskrivelse,
        String kundenavn,
        String email,
        String telefon,
        BookingStatus status,
        Long brugerId,
        Set<Long> serviceIds
) {
}