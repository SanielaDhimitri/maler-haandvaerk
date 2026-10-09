package app.controllers;

import app.dao.SecurityDAO;
import app.dto.LoginDTO;
import app.dto.RegisterDTO;
import app.entities.Person;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import app.security.ISecurityController;

import java.util.Map;

public class AuthController implements ISecurityController {

    private final SecurityDAO securityDAO;

    public AuthController(SecurityDAO securityDAO) {
        this.securityDAO = securityDAO;
    }

    // =========================
    // LOGIN
    // =========================
    @Override
    public void login(Context ctx) {

        LoginDTO loginDTO = ctx.bodyAsClass(LoginDTO.class);

        Person person = securityDAO.getVerifiedUser(
                loginDTO.email(),
                loginDTO.password()
        );

        if (person == null) {
            ctx.status(HttpStatus.UNAUTHORIZED)
                    .json(Map.of(
                            "message", "Forkert email eller password"
                    ));
            return;
        }

        ctx.status(HttpStatus.OK)
                .json(Map.of(
                        "message", "Login successful",
                        "id", person.getId(),
                        "navn", person.getNavn(),
                        "email", person.getEmail()
                ));
    }

    // =========================
    // REGISTER
    // =========================
    @Override
    public void register(Context ctx) {

        RegisterDTO registerDTO =
                ctx.bodyAsClass(RegisterDTO.class);

        Person existingPerson =
                securityDAO.findByEmail(registerDTO.email());

        if (existingPerson != null) {
            ctx.status(HttpStatus.CONFLICT)
                    .json(Map.of(
                            "message", "Bruger findes allerede"
                    ));
            return;
        }

        Person person = securityDAO.createUser(
                registerDTO.navn(),
                registerDTO.email(),
                registerDTO.password(),
                registerDTO.telefon()
        );

        ctx.status(HttpStatus.CREATED)
                .json(Map.of(
                        "message", "Bruger oprettet",
                        "id", person.getId(),
                        "navn", person.getNavn(),
                        "email", person.getEmail()
                ));
    }
    @Override
    public void authenticate(Context ctx) {

    }

    @Override
    public void authorize(Context ctx) {

    }
}
