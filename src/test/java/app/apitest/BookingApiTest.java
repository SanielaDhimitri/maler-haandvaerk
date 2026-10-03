package app.apitest;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BookingApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 7072;
    }


    // 1. TEST CREATE BOOKING - POST
    @Test
    void testCreateBooking() {

        String json = """
                {
                    "dato": "2026-10-20",
                    "tid": "10:30:00",
                    "beskrivelse": "Maling af lejlighed",
                    "kundenavn": "Test Kunde",
                    "email": "test@test.dk",
                    "telefon": "12345678",
                    "serviceIds": []
                }
                """;

        given()
                .contentType("application/json")
                .body(json)

                .when()
                .post("/api/bookings")

                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("kundenavn", equalTo("Test Kunde"))
                .body("email", equalTo("test@test.dk"))
                .body("telefon", equalTo("12345678"))
                .body("beskrivelse",
                        equalTo("Maling af lejlighed"));
    }


    // 2. TEST GET ALL BOOKINGS
    @Test
    void testGetAllBookings() {

        given()
                .when()
                .get("/api/bookings")

                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }


    // 3. TEST GET BOOKING BY ID
    @Test
    void testGetBookingById() {

        int bookingId = createBooking();

        given()
                .pathParam("id", bookingId)

                .when()
                .get("/api/bookings/{id}")

                .then()
                .statusCode(200)
                .body("id", equalTo(bookingId))
                .body("kundenavn", equalTo("Test Kunde"))
                .body("email", equalTo("test@test.dk"));
    }


    // 4. TEST UPDATE BOOKING
    @Test
    void testUpdateBooking() {

        int bookingId = createBooking();

        String updatedJson = """
                {
                    "dato": "2026-10-25",
                    "tid": "13:00:00",
                    "beskrivelse": "Opdateret maling af lejlighed",
                    "kundenavn": "Test Kunde",
                    "email": "test@test.dk",
                    "telefon": "87654321",
                    "serviceIds": []
                }
                """;

        given()
                .contentType("application/json")
                .pathParam("id", bookingId)
                .body(updatedJson)

                .when()
                .put("/api/bookings/{id}")

                .then()
                .statusCode(200)
                .body("id", equalTo(bookingId))
                .body("beskrivelse",
                        equalTo("Opdateret maling af lejlighed"))
                .body("telefon", equalTo("87654321"));
    }


    // 5. TEST DELETE BOOKING
    @Test
    void testDeleteBooking() {

        int bookingId = createBooking();

        given()
                .pathParam("id", bookingId)

                .when()
                .delete("/api/bookings/{id}")

                .then()
                .statusCode(204);


        // Kontrollerer at bookingen er slettet
        given()
                .pathParam("id", bookingId)

                .when()
                .get("/api/bookings/{id}")

                .then()
                .statusCode(404)
                .body("message",
                        equalTo("Booking not found"));
    }


    // 6. UNSUCCESSFUL SCENARIO
    // GET booking som ikke findes
    @Test
    void testGetBookingNotFound() {

        given()
                .when()
                .get("/api/bookings/999999")

                .then()
                .statusCode(404)
                .body("message",
                        equalTo("Booking not found"));
    }


    // HELPER METHOD
    // Opretter en booking og returnerer dens ID
    private int createBooking() {

        String json = """
                {
                    "dato": "2026-10-20",
                    "tid": "10:30:00",
                    "beskrivelse": "Maling af lejlighed",
                    "kundenavn": "Test Kunde",
                    "email": "test@test.dk",
                    "telefon": "12345678",
                    "serviceIds": []
                }
                """;

        return given()
                .contentType("application/json")
                .body(json)

                .when()
                .post("/api/bookings")

                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }
}