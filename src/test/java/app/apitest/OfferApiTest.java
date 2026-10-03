package app.apitest;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OfferApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 7072;
    }


    // 1. TEST CREATE OFFER - POST
    @Test
    void testCreateOffer() {

        // Opret først en WorkRequest
        int workRequestId = createWorkRequest();

        String json = """
                {
                    "pris": 15000.0,
                    "beskrivelse": "Tilbud på maling af bolig",
                    "oprettetDato": "2026-10-03",
                    "gyldigTil": "2026-10-25",
                    "workRequestId": %d
                }
                """.formatted(workRequestId);

        given()
                .contentType("application/json")
                .body(json)

                .when()
                .post("/api/offers")

                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("pris", equalTo(15000.0F))
                .body("beskrivelse",
                        equalTo("Tilbud på maling af bolig"));
    }


    // 2. TEST GET ALL OFFERS
    @Test
    void testGetAllOffers() {

        given()
                .when()
                .get("/api/offers")

                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }


    // 3. TEST GET OFFER BY ID
    @Test
    void testGetOfferById() {

        int workRequestId = createWorkRequest();

        int offerId = createOffer(workRequestId);

        given()
                .pathParam("id", offerId)

                .when()
                .get("/api/offers/{id}")

                .then()
                .statusCode(200)
                .body("id", equalTo(offerId))
                .body("pris", equalTo(15000.0F))
                .body("beskrivelse",
                        equalTo("Tilbud på maling af bolig"));
    }


    // 4. TEST UPDATE OFFER
    @Test
    void testUpdateOffer() {

        int workRequestId = createWorkRequest();

        int offerId = createOffer(workRequestId);

        String updatedJson = """
                {
                    "pris": 18000.0,
                    "beskrivelse": "Opdateret tilbud",
                    "oprettetDato": "2026-10-03",
                    "gyldigTil": "2026-10-30",
                    "workRequestId": %d
                }
                """.formatted(workRequestId);

        given()
                .contentType("application/json")
                .pathParam("id", offerId)
                .body(updatedJson)

                .when()
                .put("/api/offers/{id}")

                .then()
                .statusCode(200)
                .body("id", equalTo(offerId))
                .body("pris", equalTo(18000.0F))
                .body("beskrivelse",
                        equalTo("Opdateret tilbud"));
    }


    // 5. TEST DELETE OFFER
    // 5. TEST DELETE OFFER
    @Test
    void testDeleteOffer() {

        int workRequestId = createWorkRequest();

        int offerId = createOffer(workRequestId);

        given()
                .pathParam("id", offerId)

                .when()
                .delete("/api/offers/{id}")

                .then()
                .statusCode(204);


        // Kontrollerer at tilbuddet er slettet
        given()
                .pathParam("id", offerId)

                .when()
                .get("/api/offers/{id}")

                .then()
                .statusCode(404)
                .body("message",
                        equalTo("Offer not found"));
    }


    // 6. UNSUCCESSFUL SCENARIO
    // GET offer som ikke findes
    @Test
    void testGetOfferNotFound() {

        given()
                .when()
                .get("/api/offers/999999")

                .then()
                .statusCode(404)
                .body("message",
                        equalTo("Offer not found"));
    }


    // HELPER METHOD
    // Opretter en WorkRequest og returnerer dens ID
    private int createWorkRequest() {

        String json = """
                {
                    "fornavn": "Test",
                    "efternavn": "Kunde",
                    "email": "test@test.dk",
                    "telefon": "12345678",
                    "adresse": "Gentofte",
                    "beskrivelse": "Maling af bolig"
                }
                """;

        return given()
                .contentType("application/json")
                .body(json)

                .when()
                .post("/api/workrequests")

                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }


    // HELPER METHOD
    // Opretter et Offer og returnerer dets ID
    private int createOffer(int workRequestId) {

        String json = """
                {
                    "pris": 15000.0,
                    "beskrivelse": "Tilbud på maling af bolig",
                    "oprettetDato": "2026-10-03",
                    "gyldigTil": "2026-10-25",
                    "workRequestId": %d
                }
                """.formatted(workRequestId);

        return given()
                .contentType("application/json")
                .body(json)

                .when()
                .post("/api/offers")

                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }
}