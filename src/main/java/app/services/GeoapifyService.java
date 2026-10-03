package app.services;

import app.api.GeoapifyApi;
import app.dto.AdresseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class GeoapifyService {

    private final GeoapifyApi geoapifyApi;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GeoapifyService(String apiKey) {
        this.geoapifyApi = new GeoapifyApi(apiKey);
    }

    public List<AdresseDTO> findAdresser(String input) {

        List<AdresseDTO> adresser = new ArrayList<>();

        try {
            String json = geoapifyApi.readAPI(input);

            JsonNode root = objectMapper.readTree(json);
            JsonNode results = root.path("features");

            for (JsonNode feature : results) {

                JsonNode properties = feature.path("properties");

                AdresseDTO adresse = new AdresseDTO(
                        properties.path("formatted").asText(),
                        properties.path("street").asText(),
                        properties.path("housenumber").asText(),
                        properties.path("postcode").asText(),
                        properties.path("city").asText()
                );

                adresser.add(adresse);
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Kunne ikke hente adresser fra Geoapify",
                    e
            );
        }

        return adresser;
    }
}