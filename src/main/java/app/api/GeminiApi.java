package app.api;

import app.dto.gemini.GeminiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class GeminiApi {

    private final String apiKey;
    private final HttpClient client;
    private final ObjectMapper mapper;

    public GeminiApi(String apiKey) {
        this.apiKey = apiKey;
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public String askGemini(String prompt) {

        try {

            String businessPrompt = """
                    Du er kundeserviceassistent for DH Maler & Byggeservice.

                    Virksomheden tilbyder:
                    - Malerarbejde
                    - VVS-arbejde
                    - Elektrikerarbejde
                    - Reparationer i hus og lejlighed
                    - Montering af køkkener
                    - Montering af skabe
                    - Slibning og behandling af parketgulve
                    - Arbejde med vinduer

                    Regler:
                    - Svar på dansk.
                    - Svar kort, venligt og professionelt.
                    - Svar kun på spørgsmål, der er relevante for virksomheden og dens ydelser.
                    - Du må ikke opfinde priser.
                    - Hvis kunden spørger om pris, skal du forklare, at prisen afhænger
                      af opgaven, og at kunden kan sende en arbejdsforespørgsel for at få et tilbud.
                    - Du må ikke anbefale konkurrerende virksomheder.
                    - Du må ikke opfinde ydelser, som ikke står på listen.
                    - Hvis spørgsmålet ikke handler om virksomheden eller dens ydelser,
                      skal du venligt forklare, at du kun kan hjælpe med spørgsmål
                      om DH Maler & Byggeservice.

                    Kundens spørgsmål:
                    """ + prompt;

            String model = "gemini-3.5-flash-lite";

            String endpoint =
                    "https://generativelanguage.googleapis.com/v1beta/models/"
                            + model
                            + ":generateContent";

            Map<String, Object> body = Map.of(
                    "contents", List.of(
                            Map.of(
                                    "parts", List.of(
                                            Map.of(
                                                    "text", businessPrompt
                                            )
                                    )
                            )
                    )
            );

            String jsonBody = mapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .header("x-goog-api-key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println(
                    "HTTP status: " + response.statusCode()
            );

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "Gemini API fejl: "
                                + response.statusCode()
                                + " - "
                                + response.body()
                );
            }

            GeminiResponse geminiResponse =
                    mapper.readValue(
                            response.body(),
                            GeminiResponse.class
                    );

            return geminiResponse
                    .candidates()
                    .get(0)
                    .content()
                    .parts()
                    .get(0)
                    .text();

        } catch (Exception e) {

            // Midlertidigt så vi kan se den rigtige fejl
            e.printStackTrace();

            throw new RuntimeException(
                    "Kunne ikke kommunikere med Gemini API",
                    e
            );
        }
    }
}

//GeminiApi = kommunikerer med Gemini