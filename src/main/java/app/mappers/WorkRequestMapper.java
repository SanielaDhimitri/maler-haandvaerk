package app.mappers;

import app.dto.WorkRequestDTO;
import app.entities.WorkRequest;

import java.util.List;

public class WorkRequestMapper {

    public static WorkRequestDTO toDTO(WorkRequest workRequest) {

        return new WorkRequestDTO(
                workRequest.getId(),
                workRequest.getFornavn(),
                workRequest.getEfternavn(),
                workRequest.getEmail(),
                workRequest.getTelefon(),
                workRequest.getAdresse(),
                workRequest.getBeskrivelse(),
                workRequest.getStatus()
        );
    }

    public static List<WorkRequestDTO> toDTOList(
            List<WorkRequest> workRequests) {

        return workRequests.stream()
                .map(WorkRequestMapper::toDTO)
                .toList();
    }
}