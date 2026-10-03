package app.mappers;

import app.dto.OfferDTO;
import app.entities.Offer;

import java.util.List;

public class OfferMapper {

    // ENTITY -> DTO
    public static OfferDTO toDTO(Offer offer) {

        return new OfferDTO(
                offer.getId(),
                offer.getPris(),
                offer.getBeskrivelse(),
                offer.getOprettetDato(),
                offer.getGyldigTil(),
                offer.getStatus(),
                offer.getWorkRequest().getId()
        );
    }

    // LIST<ENTITY> -> LIST<DTO>
    public static List<OfferDTO> toDTOList(List<Offer> offers) {

        return offers.stream()
                .map(OfferMapper::toDTO)
                .toList();
    }
}