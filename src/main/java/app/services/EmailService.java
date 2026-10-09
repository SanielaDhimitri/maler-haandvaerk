package app.services;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

public class EmailService {

    private final Resend resend;

    public EmailService() {

        String apiKey = System.getenv("RESEND_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "RESEND_API_KEY mangler i environment variables"
            );
        }

        this.resend = new Resend(apiKey);
    }


    // 1. Bekræftelse når kunden sender en forespørgsel
    public void sendBekraeftelse(String modtagerEmail) {

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("DH Maler & Byggeservice <info@dhmalerogbyggeservice.dk>")
                .to(modtagerEmail)
                .subject("Bekræftelse på din arbejdsforespørgsel")
                .text("""
                        Tak for din arbejdsforespørgsel.

                        Vi har modtaget din forespørgsel og vender tilbage hurtigst muligt.

                        Med venlig hilsen
                        DH Maler & Byggeservice
                        """)
                .build();

        try {
            CreateEmailResponse response = resend.emails().send(params);

            System.out.println(
                    "Bekræftelsesmail sendt til: " + modtagerEmail
            );

            System.out.println(
                    "Resend email ID: " + response.getId()
            );

        } catch (Exception e) {
            System.out.println("Kunne ikke sende bekræftelsesmail.");
            e.printStackTrace();
        }
    }


    // 2. Send tilbud til kunden
    public void sendTilbud(
            String modtagerEmail,
            Long offerId,
            Double pris,
            String beskrivelse
    ) {

        String acceptUrl =
                "http://localhost:7072/api/offers/"
                        + offerId
                        + "/accept";

        String rejectUrl =
                "http://localhost:7072/api/offers/"
                        + offerId
                        + "/decline";

        String html = """
                <h2>DH Maler & Byggeservice</h2>

                <p>Tak for din forespørgsel.</p>

                <p><strong>Beskrivelse:</strong> %s</p>

                <p><strong>Pris:</strong> %.2f kr.</p>

                <p>Ønsker du at acceptere tilbuddet?</p>

                <p>
                    <a href="%s">JA - GODKEND TILBUD</a>
                </p>

                <p>
                    <a href="%s">NEJ - AFVIS TILBUD</a>
                </p>

                <p>
                    Med venlig hilsen<br>
                    DH Maler & Byggeservice
                </p>
                """.formatted(
                beskrivelse,
                pris,
                acceptUrl,
                rejectUrl
        );

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("DH Maler & Byggeservice <info@dhmalerogbyggeservice.dk>")
                .to(modtagerEmail)
                .subject("Dit tilbud fra DH Maler & Byggeservice")
                .html(html)
                .build();

        try {
            CreateEmailResponse response = resend.emails().send(params);

            System.out.println(
                    "Tilbudsmail sendt til: " + modtagerEmail
            );

            System.out.println(
                    "Resend email ID: " + response.getId()
            );

        } catch (Exception e) {
            System.out.println("Kunne ikke sende tilbudsmail.");
            e.printStackTrace();
        }
    }


    // 3. Mail når kunden accepterer tilbuddet
    public void sendGodkendelsesmail(String modtagerEmail) {

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("DH Maler & Byggeservice <info@dhmalerogbyggeservice.dk>")
                .to(modtagerEmail)
                .subject("Dit tilbud er godkendt")
                .text("""
                        Tak for din accept af tilbuddet.

                        Dit tilbud er nu godkendt, og vi kontakter dig snarest.

                        Med venlig hilsen
                        DH Maler & Byggeservice
                        """)
                .build();

        try {
            CreateEmailResponse response = resend.emails().send(params);

            System.out.println(
                    "Godkendelsesmail sendt til: " + modtagerEmail
            );

            System.out.println(
                    "Resend email ID: " + response.getId()
            );

        } catch (Exception e) {
            System.out.println("Kunne ikke sende godkendelsesmail.");
            e.printStackTrace();
        }
    }
}