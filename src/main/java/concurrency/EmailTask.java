package concurrency;

import app.entities.WorkRequest;
import app.services.EmailService;

public class EmailTask implements Runnable {

    private final WorkRequest request;

    public EmailTask(WorkRequest request) {
        this.request = request;
    }

    @Override
    public void run() {

        String username = "kontakt@dhmalerogbyggeservice.dk";
        String password = System.getenv("SIMPLY_EMAIL_PASSWORD");

        if (password == null || password.isBlank()) {
            System.out.println("SIMPLY_EMAIL_PASSWORD mangler.");
            return;
        }

        EmailService emailService =
                new EmailService(username, password);

        emailService.sendBekraeftelse(
                request.getEmail()
        );
    }
}

//EmailTask sender en bekræftelsesmail
// til kunden i en separat Thread.
//Formål: Programmet kan fortsætte,
// mens emailen bliver sendt