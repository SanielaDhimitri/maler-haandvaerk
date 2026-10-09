package app.dto;

import app.enums.RequestStatus;

public record UpdateWorkRequestStatusDTO(
        RequestStatus status
) {
}