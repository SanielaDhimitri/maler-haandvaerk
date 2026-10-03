package app.controllers;

import app.dao.WorkRequestDAO;

import app.entities.WorkRequest;
import app.enums.RequestStatus;
import concurrency.EmailTask;
import io.javalin.http.Context;
import app.mappers.WorkRequestMapper;
import io.javalin.http.HttpStatus;

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

        WorkRequest workRequest =
                ctx.bodyAsClass(WorkRequest.class);

        workRequest.setStatus(RequestStatus.NY);

        WorkRequest saved =
                workRequestDAO.create(workRequest);

        // Sender bekræftelsesmail i en separat tråd
        Thread emailThread = new Thread(new EmailTask(saved));
        emailThread.start();

        ctx.status(HttpStatus.CREATED)
                .json(WorkRequestMapper.toDTO(saved));
    }

    // Opdaterer status på en arbejdsforespørgsel
    public void updateStatus(Context ctx) {

        Long id = ctx.pathParamAsClass("id", Long.class)
                .check(i -> i > 0, "ID must be positive")
                .get();

        RequestStatus status =
                ctx.bodyAsClass(RequestStatus.class);

        WorkRequest updated =
                workRequestDAO.updateStatus(id, status);

        if (updated == null) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(Map.of("message", "WorkRequest not found"));
            return;
        }

        ctx.json(WorkRequestMapper.toDTO(updated));
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