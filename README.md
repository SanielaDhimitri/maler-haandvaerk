# Maler & Håndværk

Backend-projekt til håndtering af arbejdsforespørgsler, tilbud og bookinger.

Projektet er udviklet med Java, JPA/Hibernate, PostgreSQL og Javalin.

---
##User Stories
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
- ---
## Funktionalitet

Projektet indeholder blandt andet:

- Booking
- WorkRequest
- Offer
- Person/User
- Services
- Adresseopslag med Geoapify
- E-mail
- REST API
- Logging
- API-tests

---

## Tråde / Concurrency

Projektet bruger separate tråde til opgaver, som ikke skal blokere resten af programmet.

### BookingTask

`BookingTask` implementerer `Runnable`.

`Runnable` betyder, at `BookingTask` er en opgave, som en separat tråd kan udføre.

### EmailTask

`EmailTask` bruges til at sende e-mails i en separat tråd.

Det betyder, at programmet ikke behøver at vente på, at e-mailen bliver sendt, før resten af programmet kan fortsætte.

`EmailTask` bruger `EmailService`, som håndterer selve afsendelsen af e-mails.

---

## Menu

Jeg har lavet Menu-klasser for at gøre konsolprogrammet mere overskueligt:

- `MainMenu`
- `BookingMenu`
- `OfferMenu`
- `PersonMenu`
- `WorkRequestMenu`

Menu-klasserne kommunikerer med DAO- og Service-klasserne.

---

## Geoapify API

Projektet bruger Geoapify API til at søge efter og validere danske adresser.

Når brugeren indtaster en adresse, sender programmet en HTTP-request til Geoapify.

Geoapify returnerer op til 5 relevante adresser, og brugeren kan vælge den korrekte adresse.

### Implementering

- `GeoapifyApi` sender HTTP-requests.
- `GeoapifyService` behandler data fra API'et.
- Jackson bruges til JSON.
- Data konverteres til `AdresseDTO`.
- `WorkRequestMenu` viser adresserne.
- API-nøglen gemmes som environment variable: `GEOAPIFY_API_KEY`.

### Flow

`Geoapify → HTTP request → JSON → Jackson → AdresseDTO → adressevalg`

---

## Database og DAO

Projektet bruger:

- JPA/Hibernate
- PostgreSQL
- Entities og relationships
- DAO
- GenericDAO
- DTO
- Service layer

### GenericDAO

Jeg har lavet `IDAO<T>` og `GenericDAO<T>` for at undgå gentagelse af CRUD-kode.

`GenericDAO` indeholder:

- `create()`
- `findById()`
- `findAll()`
- `update()`
- `delete()`
- `count()`

DAO-klasserne kan samtidig have specifikke metoder som:

- `BookingDAO.updateStatus()`
- `OfferDAO.updateStatus()`
- `ServiceDAO.findByType()`
- `WorkRequestDAO.updateStatus()`

---

## DAO Tests

Jeg har lavet tests til:

- `BookingDAO`
- `OfferDAO`
- `PersonDAO`
- `ServiceDAO`
- `WorkRequestDAO`

Testene kontrollerer, at data kan oprettes, hentes, opdateres og slettes korrekt.

---

# Uge 40 – REST API, API Tests og Logging

## REST API

Jeg har implementeret REST endpoints til:

- WorkRequests
- Bookings
- Offers

API'et bruger:

- GET
- POST
- PUT
- DELETE

### HTTP Status Codes

- `200 OK`
- `201 Created`
- `204 No Content`
- `400 Bad Request`
- `404 Not Found`
- `500 Internal Server Error`

---

## DTO og Mapper

Jeg bruger DTO'er og Mappers, så REST API'et ikke sender JPA-entiteter direkte som JSON.

Det giver mere kontrollerede API-responses og hjælper med at undgå problemer med relationer mellem entities.

---

## API Tests

Jeg har lavet API-tests med:

- JUnit
- REST Assured
- Hamcrest

Jeg har testet:

- `WorkRequestApiTest`
- `OfferApiTest`
- `BookingApiTest`

Testene kontrollerer blandt andet:

- POST – oprettelse
- GET – hentning
- PUT – opdatering
- DELETE – sletning
- 404 Not Found

---

## Logging

Jeg har implementeret logging med SLF4J og Logback.

Applikationen logger:

- HTTP method
- Request path
- Request body
- Response status
- Response time
- Exceptions

Logs vises i konsollen og gemmes i:

`logs/app.log`

Eksempel:

```text
REQUEST: GET /api/bookings/1 | Body:
RESPONSE: GET /api/bookings/1 | Status: 200 OK | Time: 568 ms