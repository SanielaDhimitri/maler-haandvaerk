package app.mappers;

import app.dto.WorkRequestDTO;
import app.dto.WorkRequestDetailDTO;
import app.entities.WorkRequest;
import app.entities.WorkRequestDetail;

import java.util.List;

public class WorkRequestMapper {

    // =========================
    // ENTITY -> DTO
    // =========================

    public static WorkRequestDTO toDTO(WorkRequest workRequest) {

        Long brugerId =
                workRequest.getBruger() != null
                        ? workRequest.getBruger().getId()
                        : null;

        Long offerId =
                workRequest.getOffer() != null
                        ? workRequest.getOffer().getId()
                        : null;

        List<WorkRequestDetailDTO> details =
                workRequest.getWorkRequestDetails()
                        .stream()
                        .map(WorkRequestMapper::toDetailDTO)
                        .toList();

        return new WorkRequestDTO(
                workRequest.getId(),
                workRequest.getFornavn(),
                workRequest.getEfternavn(),
                workRequest.getEmail(),
                workRequest.getTelefon(),
                workRequest.getAdresse(),
                workRequest.getBeskrivelse(),
                workRequest.getStatus(),
                brugerId,
                offerId,
                details
        );
    }


    // =========================
    // WORK REQUEST DETAIL
    // ENTITY -> DTO
    // =========================

    private static WorkRequestDetailDTO toDetailDTO(
            WorkRequestDetail detail
    ) {

        return new WorkRequestDetailDTO(
                detail.getId(),
                detail.getService().getId(),
                detail.getService().getType(),
                detail.getBeskrivelse(),
                detail.getOmfang(),
                detail.getEnhed()
        );
    }


    // =========================
    // LIST<ENTITY> -> LIST<DTO>
    // =========================

    public static List<WorkRequestDTO> toDTOList(
            List<WorkRequest> workRequests
    ) {

        return workRequests.stream()
                .map(WorkRequestMapper::toDTO)
                .toList();
    }
}