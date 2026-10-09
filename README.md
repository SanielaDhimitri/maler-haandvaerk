# Maler & Håndværk

Backend-projekt til håndtering af arbejdsforespørgsler, tilbud og bookinger for en håndværksvirksomhed.

Projektet er udviklet med Java, JPA/Hibernate, PostgreSQL og Javalin.

---

## User Stories

Projektet tager udgangspunkt i følgende user stories:

- Se ydelser
- Se før/efter billeder
- Se tilbud
- Se kontaktoplysninger
- Sende arbejdsforespørgsel
- Login
- Booke en tid
- Følge eget projekt
- Skrive anmeldelse
- Administrere hjemmeside
- Administrere arbejdsforespørgsler
- Administrere projekter og bookinger

---

## Funktionalitet

Projektet indeholder blandt andet:

- Booking
- WorkRequest
- Offer
- Person/User
- Services
- Database med PostgreSQL
- JPA/Hibernate
- DAO og GenericDAO
- DTO og Mappers
- Service layer
- Adresseopslag med Geoapify API
- E-mail
- Threads og concurrency
- REST API med Javalin
- REST API-tests med REST Assured
- Unit tests med JUnit og Mockito
- Logging med SLF4J og Logback
- LLM-integration med Gemini API

---

# Database og DAO

Projektet bruger PostgreSQL som database og JPA/Hibernate til persistence.

Projektet indeholder blandt andet:

- Entities
- Relationships
- DAO
- GenericDAO
- DTO
- Mappers
- Service layer

## GenericDAO

Jeg har lavet `IDAO<T>` og `GenericDAO<T>` for at undgå gentagelse af CRUD-kode.

`GenericDAO` indeholder generelle metoder som:

- `create()`
- `findById()`
- `findAll()`
- `update()`
- `delete()`
- `count()`

De enkelte DAO-klasser kan samtidig indeholde specifikke metoder, som kun giver mening for den pågældende entity.

Eksempler:

- `BookingDAO.updateStatus()`
- `OfferDAO.updateStatus()`
- `ServiceDAO.findByType()`
- `WorkRequestDAO.updateStatus()`

---

# DAO Tests

Jeg har lavet DAO-tests til:

- `BookingDAO`
- `OfferDAO`
- `PersonDAO`
- `ServiceDAO`
- `WorkRequestDAO`

Testene kontrollerer blandt andet, at data kan:

- oprettes
- hentes
- opdateres
- slettes

DAO-tests bruges til at kontrollere integrationen mellem DAO-laget, Hibernate og databasen.

---

# Service Tests

Jeg har lavet unit tests til service-laget med JUnit og Mockito.

Jeg har tests til:

- `BookingServiceTest`
- `ChatServiceTest`
- `OfferServiceTest`

I alt har jeg 6 service-tests.

## BookingServiceTest

`BookingServiceTest` kontrollerer statusændringer på bookinger.

Testene kontrollerer:

- `bekraeftBooking()` → status ændres til `GODKENDT`
- `afvisBooking()` → status ændres til `AFVIST`
- `afslutBooking()` → status ændres til `AFSLUTTET`

`BookingDAO` mockes med Mockito, så testen ikke behøver at bruge den rigtige database.

Eksempel:

```java
BookingDAO bookingDAO = mock(BookingDAO.class);

BookingService bookingService =
        new BookingService(bookingDAO);

bookingService.bekraeftBooking(1L);

verify(bookingDAO).updateStatus(
        1L,
        BookingStatus.GODKENDT
);
```

## ChatServiceTest

`ChatServiceTest` kontrollerer, at `ChatService` kommunikerer korrekt med `GeminiApi`.

`GeminiApi` mockes med Mockito, så testen ikke sender et rigtigt request til Gemini API.

Flow:

`ChatServiceTest → ChatService → Mock GeminiApi`

## OfferServiceTest

`OfferServiceTest` kontrollerer godkendelse og afvisning af tilbud.

Ved godkendelse kontrolleres det, at:

- `Offer` ændres til `GODKENDT`
- den tilhørende `WorkRequest` ændres til `GODKENDT`

Ved afvisning kontrolleres det, at:

- `Offer` ændres til `AFVIST`
- den tilhørende `WorkRequest` ændres til `AFVIST`

`OfferDAO` og `WorkRequestDAO` mockes med Mockito.

## Mockito

Mockito bruges til at lave mock-objekter af dependencies.

Det gør det muligt at teste service-laget isoleret uden at bruge den rigtige database eller eksterne API'er.

Flow:

`JUnit → Service → Mock DAO/API → Mockito verify()`

---

# Tråde / Concurrency

Projektet bruger threads og concurrency til opgaver, som kan udføres separat.

## BookingTask

`BookingTask` implementerer `Runnable`.

`Runnable` bruges til at definere en opgave, som en thread kan udføre.

Eksempel:

```java
BookingTask task = new BookingTask(booking);

Thread thread = new Thread(task);

thread.start();
```

Flow:

`Booking → BookingTask → Thread → thread.start()`

Når `thread.start()` kaldes, starter Java en separat thread, som udfører `run()`-metoden i `BookingTask`.

## EmailTask

`EmailTask` bruges til at sende e-mails i en separat thread.

Det betyder, at programmet ikke behøver at vente på, at e-mailen bliver sendt, før resten af programmet kan fortsætte.

`EmailTask` bruger `EmailService`, som håndterer selve afsendelsen af e-mails.

Flow:

`WorkRequest → EmailTask → Thread → EmailService → SMTP`

## ExecutorService og Future

Jeg har også arbejdet med `ExecutorService` og `Future`.

`ExecutorService` kan administrere en pool af threads og udføre flere tasks.

Eksempel:

```java
ExecutorService executor =
        Executors.newFixedThreadPool(5);
```

Tasks kan sendes til thread poolen med `submit()`.

Resultatet kan gemmes i et `Future` og senere hentes med `get()`.

Det har givet mig erfaring med:

- `Runnable`
- `Thread`
- `thread.start()`
- `ExecutorService`
- Thread pools
- `submit()`
- `Future`
- `get()`
- Concurrency

---

# Geoapify API

Projektet bruger Geoapify API til adresseopslag.

Når brugeren indtaster en adresse, sender programmet et HTTP-request til Geoapify.

Geoapify returnerer relevante adresseforslag, som konverteres til Java-objekter.

## Implementering

- `GeoapifyApi` kommunikerer med Geoapify API.
- `GeoapifyService` behandler data.
- Jackson bruges til JSON.
- Data konverteres til `AdresseDTO`.
- Adresseforslag kan vises for brugeren.
- API-nøglen gemmes som environment variable.

Environment variable:

```text
GEOAPIFY_API_KEY
```

## Flow

`Adresse → GeoapifyService → GeoapifyApi → HTTP Request → Geoapify → JSON → Jackson → AdresseDTO`

---

# REST API

Jeg har implementeret et REST API med Javalin.

REST API'et gør det muligt for en klient at kommunikere med backend-systemet gennem HTTP.

Jeg har blandt andet REST endpoints til:

- WorkRequests
- Bookings
- Offers
- Chat

API'et bruger HTTP-metoderne:

- `GET`
- `POST`
- `PUT`
- `DELETE`

---

## Booking Endpoints

| Method | Endpoint | Beskrivelse |
|---|---|---|
| GET | `/api/bookings` | Hent alle bookinger |
| GET | `/api/bookings/{id}` | Hent booking efter ID |
| POST | `/api/bookings` | Opret booking |
| PUT | `/api/bookings/{id}` | Opdater booking |
| DELETE | `/api/bookings/{id}` | Slet booking |

---

## Offer Endpoints

| Method | Endpoint | Beskrivelse |
|---|---|---|
| GET | `/api/offers` | Hent alle tilbud |
| GET | `/api/offers/{id}` | Hent tilbud efter ID |
| POST | `/api/offers` | Opret tilbud |
| PUT | `/api/offers/{id}` | Opdater tilbud |
| PUT | `/api/offers/{id}/approve` | Godkend tilbud |
| PUT | `/api/offers/{id}/reject` | Afvis tilbud |
| DELETE | `/api/offers/{id}` | Slet tilbud |

Når et tilbud godkendes gennem `/approve`, opdaterer `OfferService` både tilbuddets status og den tilhørende WorkRequest.

Det samme sker ved afvisning gennem `/reject`.

---

## WorkRequest Endpoints

| Method | Endpoint | Beskrivelse |
|---|---|---|
| GET | `/api/workrequests` | Hent alle arbejdsforespørgsler |
| GET | `/api/workrequests/{id}` | Hent arbejdsforespørgsel efter ID |
| POST | `/api/workrequests` | Opret arbejdsforespørgsel |
| PUT | `/api/workrequests/{id}/status` | Opdater status |
| DELETE | `/api/workrequests/{id}` | Slet arbejdsforespørgsel |

Ved oprettelse af en WorkRequest bruges `CreateWorkRequestDTO`.

Data valideres i controlleren, før der oprettes en `WorkRequest`.

En ny WorkRequest starter med status:

```text
NY
```

---

## Chat Endpoint

| Method | Endpoint | Beskrivelse |
|---|---|---|
| POST | `/api/chat` | Send spørgsmål til chatbot |

---

# HTTP Status Codes

REST API'et bruger blandt andet følgende HTTP status codes:

- `200 OK` – request gennemført korrekt
- `201 Created` – ny resource oprettet
- `204 No Content` – resource slettet
- `400 Bad Request` – ugyldigt request
- `404 Not Found` – resource findes ikke
- `500 Internal Server Error` – serverfejl

---

# Controllers

Controllers håndterer HTTP-requests fra klienten.

Jeg har blandt andet:

- `BookingController`
- `OfferController`
- `WorkRequestController`
- `ChatController`

Et eksempel på flow for en booking er:

`HTTP Request → Routes → BookingController → BookingDAO → Database`

Controlleren bestemmer også HTTP status code og JSON-response.

---

# DTO og Mapper

Jeg bruger DTO'er og Mappers, så REST API'et ikke behøver at sende JPA-entiteter direkte som JSON.

DTO'er bruges til at kontrollere, hvilke data der sendes ind og ud af REST API'et.

Mappers bruges til at konvertere mellem entities og DTO'er.

Projektet indeholder blandt andet:

- `BookingDTO`
- `OfferDTO`
- `OfferRequestDTO`
- `WorkRequestDTO`
- `CreateWorkRequestDTO`
- `WorkRequestDetailDTO`
- `AdresseDTO`
- `ChatRequestDTO`

Et typisk response-flow er:

`HTTP Request → Controller → DAO → Entity → Mapper → DTO → JSON Response`

Ved oprettelse kan flowet eksempelvis være:

`JSON Request → CreateWorkRequestDTO → Controller → WorkRequest → DAO → Database`

Det giver mere kontrollerede API-responses og hjælper med at undgå problemer med relationer mellem entities.

---

# REST API Tests med REST Assured

Jeg har lavet automatiserede REST API-tests med:

- JUnit
- REST Assured
- Hamcrest

REST Assured bruges til automatisk at sende HTTP-requests til REST API'et og kontrollere responses.

Jeg har blandt andet lavet:

- `WorkRequestApiTest`
- `OfferApiTest`
- `BookingApiTest`

Testene kontrollerer blandt andet:

- POST – oprettelse
- GET – hentning
- PUT – opdatering
- DELETE – sletning
- HTTP status codes
- JSON response data
- `404 Not Found`

## Eksempel

```java
given()
        .when()
        .get("/api/bookings")
        .then()
        .statusCode(200);
```

REST Assured bruger en BDD-lignende struktur:

`given() → when() → then()`

- `given()` beskriver forudsætningerne for requestet.
- `when()` udfører requestet.
- `then()` kontrollerer resultatet.

Hamcrest bruges til assertions som:

```java
.body("status", equalTo("NY"))
.body("id", notNullValue());
```

Det betyder, at mine REST API-tests ikke kun kontrollerer statuskoden, men også indholdet i JSON-responsen.

---

# Manuel API-test

Ud over de automatiserede REST Assured-tests har jeg også brugt `.http` requests til manuelt at teste mine endpoints.

Eksempel:

```http
GET http://localhost:7072/api/bookings
```

Eksempel på POST:

```http
POST http://localhost:7072/api/bookings
Content-Type: application/json

{
  "dato": "2026-10-20",
  "tid": "10:30:00",
  "beskrivelse": "Maling af lejlighed",
  "kundenavn": "Test Kunde",
  "email": "test@test.dk",
  "telefon": "12345678",
  "serviceIds": []
}
```

Forskellen er:

- `.http` bruges til manuel test.
- REST Assured bruges til automatiseret REST API-test.

---

# Logging

Jeg har implementeret logging med SLF4J og Logback.

Applikationen logger blandt andet:

- HTTP method
- Request path
- Request body
- Response status
- Response time
- Exceptions

Logs vises i konsollen og gemmes i:

```text
logs/app.log
```

Eksempel:

```text
REQUEST: GET /api/bookings/1 | Body:
RESPONSE: GET /api/bookings/1 | Status: 200 OK | Time: 568 ms
```

Logging gør det lettere at følge requests gennem systemet og finde fejl.

---

# E-mail

Projektet kan sende en bekræftelsesmail, når der oprettes en arbejdsforespørgsel.

`EmailTask` udfører e-mailopgaven i en separat thread.

`EmailService` håndterer selve afsendelsen gennem Jakarta Mail og SMTP.

Flow:

`WorkRequest → EmailTask → EmailService → Jakarta Mail → SMTP`

Det betyder, at e-mailafsendelsen kan udføres separat fra det normale request-flow.

---

# LLM – Gemini AI Chatbot

Jeg har integreret en LLM (Large Language Model) ved hjælp af Gemini API.

LLM'en fungerer som kundeserviceassistent for DH Maler & Byggeservice.

Kunden kan stille spørgsmål om virksomhedens ydelser gennem REST API'et, og spørgsmålet sendes videre til Gemini.

---

## Gemini API Key

Gemini API-nøglen gemmes som en environment variable:

```text
GEMINI_API_KEY
```

API-nøglen læses i Java med:

```java
String geminiApiKey =
        System.getenv("GEMINI_API_KEY");
```

API-nøglen skal derfor ikke skrives direkte i kildekoden.

---

## Business Prompt

Jeg har lavet et business prompt, som giver Gemini information og regler for, hvordan chatbotten skal svare.

Promptet fortæller blandt andet:

- hvilke ydelser virksomheden tilbyder
- at Gemini skal svare på dansk
- at svaret skal være kort og professionelt
- at Gemini ikke må opfinde priser
- at Gemini ikke må anbefale konkurrerende virksomheder
- at Gemini ikke må opfinde ydelser
- at Gemini kun skal svare på spørgsmål, der er relevante for virksomheden

Virksomhedens ydelser i promptet omfatter blandt andet:

- Malerarbejde
- VVS-arbejde
- Elektrikerarbejde
- Reparationer i hus og lejlighed
- Montering af køkkener
- Montering af skabe
- Slibning og behandling af parketgulve
- Arbejde med vinduer

---

## GeminiApi

`GeminiApi` håndterer kommunikationen med Gemini API.

Klassen bruger blandt andet:

- `HttpClient`
- `HttpRequest`
- `HttpResponse`
- Jackson `ObjectMapper`

Processen er:

1. Kundens spørgsmål modtages.
2. Spørgsmålet kombineres med virksomhedens business prompt.
3. Java opbygger JSON-requestet.
4. Et HTTP POST-request sendes til Gemini API.
5. Gemini genererer et svar.
6. Gemini returnerer JSON.
7. Jackson konverterer JSON til Java-objekter.
8. Teksten fra svaret returneres til applikationen.

---

## Gemini Response

Gemini returnerer ikke kun en almindelig String.

Svaret kommer som en JSON-struktur med flere niveauer.

Jeg har derfor lavet Java records, som matcher den del af Gemini-responsen, som applikationen skal bruge:

`GeminiResponse → Candidate → Content → Part → text`

De fire records repræsenterer strukturen:

- `GeminiResponse`
- `Candidate`
- `Content`
- `Part`

Til sidst kan selve teksten hentes fra `Part`.

Jackson `ObjectMapper` bruges til at konvertere JSON-responsen til disse Java-objekter.

---

## ChatController

`ChatController` håndterer HTTP-requestet fra kunden.

Controlleren læser spørgsmålet fra request body gennem `ChatRequestDTO`.

Eksempel:

```json
{
  "question": "Kan I hjælpe med VVS arbejde?"
}
```

Hvis `question` mangler eller er tom, returnerer API'et:

```text
400 Bad Request
```

Hvis spørgsmålet er gyldigt, sendes det videre til `ChatService`, som bruger `GeminiApi`.

Svaret returneres derefter som JSON.

Eksempel:

```json
{
  "answer": "Ja, det kan vi bestemt! Vi tilbyder professionelt VVS-arbejde."
}
```

---

## LLM Flow

Det samlede flow for chatbotten er:

`Kunde → POST /api/chat → ChatController → ChatService → GeminiApi → Business Prompt → Gemini API → JSON Response → Java records → svar til kunden`

Det betyder, at Gemini er den LLM, der genererer svaret, mens Java-applikationen bestemmer, hvordan spørgsmålet sendes til modellen, hvilke business-regler den får, og hvordan svaret behandles.

---

# Environment Variables

Projektet bruger environment variables til API-nøgler.

```text
GEOAPIFY_API_KEY
GEMINI_API_KEY
```

På denne måde behøver API-nøglerne ikke at ligge direkte i kildekoden.

---

# Teknologier

Projektet bruger blandt andet:

- Java
- Javalin
- JPA
- Hibernate
- PostgreSQL
- Jackson
- REST API
- REST Assured
- JUnit
- Mockito
- Hamcrest
- SLF4J
- Logback
- Geoapify API
- Gemini API / LLM
- Jakarta Mail
- SMTP
- Java Threads
- ExecutorService
- Future

---

# Samlet arkitektur

En almindelig REST-request følger eksempelvis dette flow:

`Client → Javalin Routes → Controller → DAO → Hibernate → PostgreSQL`

DTO-flow:

`Entity → Mapper → DTO → JSON Response`

Service unit tests følger:

`JUnit → Service → Mock DAO/API → Mockito verify()`

REST API-tests følger:

`JUnit → REST Assured → REST API → Controller → DAO → Database → JSON Response → Hamcrest validation`

Geoapify-integrationen følger:

`Java → GeoapifyService → GeoapifyApi → Geoapify API → JSON → Jackson → AdresseDTO`

E-mail følger:

`WorkRequest → EmailTask → Thread → EmailService → SMTP`

LLM-integrationen følger:

`Kunde → ChatController → ChatService → GeminiApi → Gemini LLM → JSON → Java records → Kunde`

---

# Testoversigt

Projektet indeholder flere forskellige typer tests:

### DAO-tests

Tester integrationen mellem DAO, Hibernate og databasen.

### Service-tests

Tester business logic isoleret ved hjælp af Mockito.

De 6 service-tests er fordelt på:

- `BookingServiceTest` – 3 tests
- `ChatServiceTest` – 1 test
- `OfferServiceTest` – 2 tests

### REST API-tests

Tester REST endpoints med REST Assured og Hamcrest.

På denne måde testes applikationen på flere niveauer:

`DAO → Service → REST API`

## Email API

Projektet bruger Resend API til automatisk at sende emails til kunder.

Når der oprettes et tilbud, sender systemet tilbuddet til kundens email.
Kunden kan acceptere eller afvise tilbuddet direkte fra emailen.

Resend API-nøglen gemmes som en environment variable:

`RESEND_API_KEY`

---

# Security

Jeg har startet implementeringen af security i projektet.

Security-laget bruges til registrering og login af brugere samt sikker håndtering af passwords.

## Password Hashing

Passwords gemmes ikke som almindelig tekst i databasen.

Jeg bruger BCrypt til at hashe passwords, før de gemmes.

Når en `Person` oprettes, bliver passwordet hashed med:

`BCrypt.hashpw()`

Ved login kontrolleres passwordet med:

`BCrypt.checkpw()`

Flow:

`Password → BCrypt → Hash → Database`

Det betyder, at det oprindelige password ikke gemmes direkte i databasen.

---

## Person og ISecurityUser

`Person` implementerer `ISecurityUser`.

Interfacet definerer de security-funktioner, som en bruger skal have:

- `verifyPassword()`
- `addRole()`
- `removeRole()`
- `getRolesAsStrings()`

Det gør security-strukturen mere ensartet og adskiller security-kontrakten fra implementeringen.

---

## Roles

Projektet har en `Role` entity.

En `Person` kan have flere roller gennem en `ManyToMany` relation.

Relationen gemmes i tabellen:

`person_role`

Roller skal senere bruges til authorization, så forskellige endpoints kan beskyttes afhængigt af brugerens rolle.

Eksempler:

- USER
- ADMIN

---

## SecurityDAO

Jeg har lavet `ISecurityDAO` og `SecurityDAO`.

`SecurityDAO` håndterer funktionalitet relateret til brugere og security.

Det bruges blandt andet til:

- at finde en bruger via email
- at verificere email og password ved login
- at oprette nye brugere
- at oprette roller
- at tildele roller til brugere

Login-flow:

`Email + Password → SecurityDAO → Person → BCrypt verification`

---

## Register

Jeg har lavet et register endpoint:

`POST /api/auth/register`

Ved registrering kontrollerer systemet først, om emailen allerede findes.

Hvis brugeren ikke findes, oprettes en ny bruger, og passwordet hashes med BCrypt før det gemmes i databasen.

Et succesfuldt register-request returnerer:

`201 Created`

Hvis emailen allerede findes, returneres:

`409 Conflict`

---

## Login

Jeg har lavet et login endpoint:

`POST /api/auth/login`

Login bruger `SecurityDAO.getVerifiedUser()` til at kontrollere email og password.

Hvis oplysningerne er korrekte, returnerer API'et:

`200 OK`

Hvis email eller password er forkert, returnerer API'et:

`401 Unauthorized`

Flow:

`POST /api/auth/login → AuthController → SecurityDAO → BCrypt → Person`

---

## SecurityController Interface

`AuthController` implementerer `ISecurityController`.

Interfacet indeholder:

- `login(Context ctx)`
- `register(Context ctx)`
- `authenticate(Context ctx)`
- `authorize(Context ctx)`

`login()` og `register()` er implementeret.

`authenticate()` og `authorize()` er oprettet og skal bruges sammen med JWT-security.

---

## GitHub Actions – Continuous Integration (CI)

Jeg har opsat GitHub Actions til automatisk at bygge og teste mit Java-backendprojekt.

Workflow-filen ligger i:

`.github/workflows/maven.yml`

Når jeg pusher kode til `main`, starter GitHub Actions automatisk.

Workflowet udfører følgende:

1. Henter projektets kode fra GitHub.
2. Installerer Java 25.
3. Starter en PostgreSQL 16-database.
4. Kompilerer Maven-projektet.
5. Starter Javalin-serveren på port 7072.
6. Kører automatiserede tests med `mvn verify`.

### E-mail under tests

Jeg bruger environment variablen `EMAIL_TEST_MODE=true` i GitHub Actions.

Det betyder, at e-mails simuleres under CI, så testene ikke sender rigtige e-mails gennem Resend API.

### Resultat

GitHub Actions workflowet er gennemført med succes.

CI hjælper mig med automatisk at opdage fejl, når jeg ændrer eller tilføjer kode.

Deployment er endnu ikke implementeret og bliver arbejdet med senere.


