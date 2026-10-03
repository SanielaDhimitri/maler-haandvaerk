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
            config.router.apiBuilder(routes.getRoutes());
        });

        // Logger request
        app.before(ctx -> {

            ctx.attribute("startTime", System.currentTimeMillis());

            logger.info(
                    "REQUEST: {} {} | Body: {}",
                    ctx.method(),
                    ctx.path(),
                    ctx.body()
            );
        });


        // Logger response
        app.after(ctx -> {

            Long startTime = ctx.attribute("startTime");

            long responseTime = startTime != null
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


        // Logger fejl
        app.exception(Exception.class, (e, ctx) -> {

            logger.error(
                    "ERROR: {} {}",
                    ctx.method(),
                    ctx.path(),
                    e
            );

            ctx.status(500)
                    .json(java.util.Map.of(
                            "message",
                            "Internal Server Error"
                    ));
        });
    }

    public void startServer(int port) {
        app.start(port);
    }

    public void stopServer() {
        app.stop();
    }
}