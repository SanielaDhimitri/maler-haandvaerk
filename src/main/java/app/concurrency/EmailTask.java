package app.concurrency;

import app.entities.WorkRequest;
import app.services.EmailService;

public class EmailTask implements Runnable {

    private final WorkRequest request;

    public EmailTask(WorkRequest request) {
        this.request = request;
    }

    @Override
    public void run() {

        EmailService emailService = new EmailService();

        emailService.sendBekraeftelse(
                request.getEmail()
        );
    }
}