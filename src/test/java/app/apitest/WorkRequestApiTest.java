package app.apitest;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class WorkRequestApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 7072;
    }


    // 1. TEST CREATE WORKREQUEST - POST
    @Test
    void testCreateWorkRequest() {

        String json = """
                {
                    "fornavn": "Test",
                    "efternavn": "Kunde",
                    "email": "test@mail.dk",
                    "telefon": "12345678",
                    "adresse": "Gentofte",
                    "beskrivelse": "Maling af lejlighed"
                }
                """;

        given()
                .contentType("application/json")
                .body(json)

                .when()
                .post("/api/workrequests")

                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("fornavn", equalTo("Test"))
                .body("efternavn", equalTo("Kunde"))
                .body("email", equalTo("test@mail.dk"))
                .body("telefon", equalTo("12345678"))
                .body("adresse", equalTo("Gentofte"))
                .body("beskrivelse", equalTo("Maling af lejlighed"))
                .body("status", equalTo("NY"));
    }


    // 2. TEST GET ALL WORKREQUESTS
    @Test
    void testGetAllWorkRequests() {

        given()
                .when()
                .get("/api/workrequests")

                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }


    // 3. TEST GET ONE WORKREQUEST
    @Test
    void testGetWorkRequestById() {

        String json = """
                {
                    "fornavn": "Anna",
                    "efternavn": "Test",
                    "email": "anna@test.dk",
                    "telefon": "87654321",
                    "adresse": "Lyngby",
                    "beskrivelse": "Maling af stue"
                }
                """;

        int id =
                given()
                        .contentType("application/json")
                        .body(json)

                        .when()
                        .post("/api/workrequests")

                        .then()
                        .statusCode(201)
                        .extract()
                        .path("id");


        given()
                .pathParam("id", id)

                .when()
                .get("/api/workrequests/{id}")

                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("fornavn", equalTo("Anna"))
                .body("efternavn", equalTo("Test"))
                .body("email", equalTo("anna@test.dk"))
                .body("status", equalTo("NY"));
    }


    // 4. TEST UPDATE STATUS
    @Test
    void testUpdateWorkRequestStatus() {

        String json = """
                {
                    "fornavn": "Peter",
                    "efternavn": "Test",
                    "email": "peter@test.dk",
                    "telefon": "11223344",
                    "adresse": "Gentofte",
                    "beskrivelse": "Renovering"
                }
                """;

        int id =
                given()
                        .contentType("application/json")
                        .body(json)

                        .when()
                        .post("/api/workrequests")

                        .then()
                        .statusCode(201)
                        .extract()
                        .path("id");


        given()
                .contentType("application/json")
                .pathParam("id", id)
                .body("\"UNDER_BEHANDLING\"")

                .when()
                .put("/api/workrequests/{id}/status")

                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("status", equalTo("UNDER_BEHANDLING"));
    }


    // 5. TEST DELETE WORKREQUEST
    @Test
    void testDeleteWorkRequest() {

        String json = """
                {
                    "fornavn": "Delete",
                    "efternavn": "Test",
                    "email": "delete@test.dk",
                    "telefon": "12345678",
                    "adresse": "Gentofte",
                    "beskrivelse": "Denne skal slettes"
                }
                """;

        int id =
                given()
                        .contentType("application/json")
                        .body(json)

                        .when()
                        .post("/api/workrequests")

                        .then()
                        .statusCode(201)
                        .extract()
                        .path("id");


        given()
                .pathParam("id", id)

                .when()
                .delete("/api/workrequests/{id}")

                .then()
                .statusCode(204);


        // Kontrollerer at WorkRequest er slettet
        given()
                .pathParam("id", id)

                .when()
                .get("/api/workrequests/{id}")

                .then()
                .statusCode(404)
                .body("message",
                        equalTo("WorkRequest not found"));
    }


    // 6. UNSUCCESSFUL SCENARIO
    // GET WorkRequest som ikke findes
    @Test
    void testGetWorkRequestNotFound() {

        given()
                .when()
                .get("/api/workrequests/999999")

                .then()
                .statusCode(404)
                .body("message",
                        equalTo("WorkRequest not found"));
    }
}