package app.services;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {

    private final String username;
    private final String password;

    public EmailService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void sendBekraeftelse(String modtagerEmail) {

        Properties properties = new Properties();

        properties.put("mail.smtp.host", "smtp.simply.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(
                properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                }
        );

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(username));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(modtagerEmail)
            );

            message.setSubject(
                    "Bekræftelse på din arbejdsforespørgsel"
            );

            message.setText(
                    "Tak for din arbejdsforespørgsel.\n\n" +
                            "Vi har modtaget din forespørgsel og vender tilbage hurtigst muligt.\n\n" +
                            "Med venlig hilsen\n" +
                            "DH Maler & Byggeservice"
            );

            Transport.send(message);

            System.out.println(
                    "Bekræftelsesmail sendt til: " + modtagerEmail
            );

        } catch (MessagingException e) {

            System.out.println("Kunne ikke sende email.");
            e.printStackTrace();
        }
    }
}

// EMAIL
// EmailTask
//    ↓
// EmailService
//    ↓
// Jakarta Mail
//    ↓
// SMTP
//    ↓
// smtp.simply.com

// Geoapify: GeoapifyService → GeoapifyApi → REST API

// Email: EmailTask → EmailService → SMTP-server
// Email bruger ikke REST API, men SMTP til at sende mails.

// LLM: LLMService → GeminiApi → Gemini API