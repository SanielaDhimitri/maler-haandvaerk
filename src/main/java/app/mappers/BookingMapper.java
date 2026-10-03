package app.mappers;

import app.dto.BookingDTO;
import app.entities.Booking;
import app.entities.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookingMapper {

    // Entity -> DTO
    public static BookingDTO toDTO(Booking booking) {

        Set<Long> serviceIds = booking.getServices()
                .stream()
                .map(Service::getId)
                .collect(Collectors.toSet());

        return new BookingDTO(
                booking.getId(),
                booking.getDato(),
                booking.getTid(),
                booking.getBeskrivelse(),
                booking.getKundenavn(),
                booking.getEmail(),
                booking.getTelefon(),
                booking.getStatus(),
                booking.getBruger() != null
                        ? booking.getBruger().getId()
                        : null,
                serviceIds
        );
    }

    // DTO -> Entity
    public static Booking toEntity(BookingDTO dto) {

        Booking booking = new Booking(
                dto.dato(),
                dto.tid(),
                dto.beskrivelse(),
                dto.kundenavn(),
                dto.email(),
                dto.telefon(),
                null
        );

        if (dto.status() != null) {
            booking.setStatus(dto.status());
        }

        return booking;
    }

    // List<Entity> -> List<DTO>
    public static List<BookingDTO> toDTOList(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toDTO)
                .toList();
    }
}