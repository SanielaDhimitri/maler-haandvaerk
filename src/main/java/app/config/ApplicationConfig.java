package app.config;

import app.routes.Routes;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationConfig {

    private static final Logger logger =
            LoggerFactory.getLogger(ApplicationConfig.class);

    private final Javalin app;

    public ApplicationConfig(Routes routes) {

        app = Javalin.create(config -> {

            // =========================
            // ROUTES
            // Registrerer alle API endpoints
            // =========================
            routes.register(config);


            // =========================
            // BEFORE HANDLER
            // Kører før alle HTTP requests
            // Logger request og gemmer starttid
            // =========================
            config.routes.before(ctx -> {

                ctx.attribute(
                        "startTime",
                        System.currentTimeMillis()
                );

                logger.info(
                        "REQUEST: {} {} | Body: {}",
                        ctx.method(),
                        ctx.path(),
                        ctx.body()
                );
            });


            // =========================
            // AFTER HANDLER
            // Kører efter alle HTTP requests
            // Logger status og response time
            // =========================
            config.routes.after(ctx -> {

                Long startTime =
                        ctx.attribute("startTime");

                long responseTime =
                        startTime != null
                                ? System.currentTimeMillis() - startTime
                                : 0;

                logger.info(
                        "RESPONSE: {} {} | Status: {} | Time: {} ms",
                        ctx.method(),
                        ctx.path(),
                        ctx.status(),
                        responseTime
                );
            });


            // =========================
            // EXCEPTION HANDLER
            // Håndterer uventede fejl
            // Returnerer HTTP 500 som tekst
            // =========================
            config.routes.exception(Exception.class, (e, ctx) -> {

                logger.error(
                        "ERROR: {} {}",
                        ctx.method(),
                        ctx.path(),
                        e
                );

                ctx.status(500);
                ctx.result("Internal Server Error");
            });
        });
    }


    // =========================
    // START SERVER
    // =========================
    public void startServer(int port) {
        app.start(port);
    }


    // =========================
    // STOP SERVER
    // =========================
    public void stopServer() {
        app.stop();
    }
}