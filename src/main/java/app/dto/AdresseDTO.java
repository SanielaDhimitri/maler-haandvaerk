package app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AdresseDTO(
        String formatted,
        String street,
        String housenumber,
        String postcode,
        String city
) {
}