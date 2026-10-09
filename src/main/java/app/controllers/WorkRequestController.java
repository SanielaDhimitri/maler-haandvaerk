package app.controllers;

import app.dao.WorkRequestDAO;

import app.dto.UpdateWorkRequestStatusDTO;
import app.entities.WorkRequest;
import app.concurrency.EmailTask;
import io.javalin.http.Context;
import app.mappers.WorkRequestMapper;
import io.javalin.http.HttpStatus;
import app.dto.CreateWorkRequestDTO;

import java.util.Map;

public class WorkRequestController {

    private final WorkRequestDAO workRequestDAO;

    public WorkRequestController(WorkRequestDAO workRequestDAO) {
        this.workRequestDAO = workRequestDAO;
    }
    // Henter alle arbejdsforespørgsler
    public void getAll(Context ctx) {

        var workRequests = workRequestDAO.findAll();

        ctx.json(WorkRequestMapper.toDTOList(workRequests));
    }
    // Henter én arbejdsforespørgsel ud fra ID
    public void getById(Context ctx) {

        Long id = ctx.pathParamAsClass("id", Long.class)
                .check(i -> i > 0, "ID must be positive")
                .get();

        var workRequest = workRequestDAO.findById(id);

        if (workRequest == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of("message", "WorkRequest not found"));
            return;
        }

        ctx.json(WorkRequestMapper.toDTO(workRequest));
    }

    // Opretter en arbejdsforespørgsel
    public void create(Context ctx) {

        CreateWorkRequestDTO request =
                ctx.bodyValidator(CreateWorkRequestDTO.class)

                        .check(
                                r -> r.fornavn() != null
                                        && !r.fornavn().isBlank(),
                                "Fornavn må ikke være tomt"
                        )

                        .check(
                                r -> r.efternavn() != null
                                        && !r.efternavn().isBlank(),
                                "Efternavn må ikke være tomt"
                        )

                        .check(
                                r -> r.email() != null
                                        && r.email().matches(
                                        "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"
                                ),
                                "Email skal være gyldig"
                        )

                        .check(
                                r -> r.telefon() != null
                                        && r.telefon().matches("\\d{8}"),
                                "Telefonnummer skal bestå af 8 cifre"
                        )

                        .check(
                                r -> r.adresse() != null
                                        && !r.adresse().isBlank(),
                                "Adresse må ikke være tom"
                        )

                        .check(
                                r -> r.beskrivelse() != null
                                        && !r.beskrivelse().isBlank(),
                                "Beskrivelse må ikke være tom"
                        )

                        .get();

        WorkRequest workRequest = new WorkRequest(
                request.fornavn(),
                request.efternavn(),
                request.email(),
                request.telefon(),
                request.adresse(),
                request.beskrivelse(),
                null
        );

        WorkRequest saved =
                workRequestDAO.create(workRequest);

        // Sender bekræftelsesmail i en separat tråd
        Thread emailThread =
                new Thread(new EmailTask(saved));

        emailThread.start();

        ctx.status(HttpStatus.CREATED)
                .json(WorkRequestMapper.toDTO(saved));
    }

    // Opdaterer status på en arbejdsforespørgsel
    public void updateStatus(Context ctx) {

        Long id = ctx.pathParamAsClass("id", Long.class)
                .check(i -> i > 0, "ID must be positive")
                .get();

        UpdateWorkRequestStatusDTO request =
                ctx.bodyValidator(UpdateWorkRequestStatusDTO.class)
                        .check(
                                r -> r.status() != null,
                                "Status skal angives"
                        )
                        .get();

        WorkRequest updated =
                workRequestDAO.updateStatus(id, request.status());

        if (updated == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of(
                            "message", "WorkRequest not found"
                    ));
            return;
        }

        ctx.status(HttpStatus.OK)
                .json(WorkRequestMapper.toDTO(updated));
    }

    // Sletter en arbejdsforespørgsel
    public void delete(Context ctx) {

        Long id = ctx.pathParamAsClass("id", Long.class)
                .check(i -> i > 0, "ID must be positive")
                .get();

        WorkRequest workRequest = workRequestDAO.findById(id);

        if (workRequest == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of("message", "WorkRequest not found"));
            return;
        }

        workRequestDAO.delete(id);

        ctx.status(HttpStatus.NO_CONTENT)
           .json(Map.of("message", "WorkRequest deleted successfully"));
    }
}