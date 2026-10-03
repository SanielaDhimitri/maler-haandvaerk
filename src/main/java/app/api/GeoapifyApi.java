package app.api;//kommuniker med geoapify db gennem http request

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class GeoapifyApi {

    private final String apiKey;

    public GeoapifyApi(String apiKey) {
        this.apiKey = apiKey;
    }

    public String readAPI(String input) {

        try {
            String encodedInput =
                    URLEncoder.encode(input, StandardCharsets.UTF_8);

            String url =
                    "https://api.geoapify.com/v1/geocode/autocomplete"
                            + "?text=" + encodedInput
                            + "&filter=countrycode:dk"
                            + "&limit=5"
                            + "&apiKey=" + apiKey;

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "Geoapify fejl: " + response.statusCode()
                );
            }

            return response.body();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

