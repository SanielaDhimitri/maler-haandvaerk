# API Documentation – DH Maler & Byggeservice

The application provides a REST API built with Javalin.

The API handles work requests, offers, bookings and AI chat.

## Base URL

```text
http://localhost:7072/api
```

---

# Work Requests

| Method | URL | Request Body (JSON) | Response (JSON) | Error |
|--------|-----|---------------------|-----------------|-------|
| GET | /workrequests | | [workRequest, ...] | |
| GET | /workrequests/{id} | | workRequest | (e1) |
| POST | /workrequests | workRequest without id and status | created workRequest | (e2) |
| PUT | /workrequests/{id}/status | status | updated workRequest | (e1) |
| DELETE | /workrequests/{id} | | | (e1) |

## WorkRequest format

A WorkRequest contains information about a customer's work request.

```json
{
  "id": 1,
  "fornavn": "Anna",
  "efternavn": "Hansen",
  "email": "anna@test.dk",
  "telefon": "12345678",
  "adresse": "Gentofte",
  "beskrivelse": "Maling af stue",
  "status": "NY",
  "brugerId": null,
  "offerId": null,
  "workRequestDetails": []
}
```

For POST requests, the client does not provide `id` or `status`.

A new WorkRequest automatically gets the status `NY`.

## Create WorkRequest

### Request

```json
{
  "fornavn": "Anna",
  "efternavn": "Hansen",
  "email": "anna@test.dk",
  "telefon": "12345678",
  "adresse": "Gentofte",
  "beskrivelse": "Maling af stue"
}
```

### Response

```json
{
  "id": 1,
  "fornavn": "Anna",
  "efternavn": "Hansen",
  "email": "anna@test.dk",
  "telefon": "12345678",
  "adresse": "Gentofte",
  "beskrivelse": "Maling af stue",
  "status": "NY",
  "brugerId": null,
  "offerId": null,
  "workRequestDetails": []
}
```

HTTP status:

```text
201 Created
```

---

## Update WorkRequest status

Endpoint:

```text
PUT /api/workrequests/{id}/status
```

Example request:

```json
{
  "status": "UNDER_BEHANDLING"
}
```

Possible WorkRequest statuses:

```text
NY
UNDER_BEHANDLING
TILBUD_SENDT
GODKENDT
AFVIST
```

Example response:

```json
{
  "id": 1,
  "fornavn": "Anna",
  "efternavn": "Hansen",
  "email": "anna@test.dk",
  "telefon": "12345678",
  "adresse": "Gentofte",
  "beskrivelse": "Maling af stue",
  "status": "UNDER_BEHANDLING",
  "brugerId": null,
  "offerId": null,
  "workRequestDetails": []
}
```

HTTP status:

```text
200 OK
```

---

# Offers

The Offer API is used to manage offers connected to work requests.

| Method | URL | Request Body (JSON) | Response (JSON) | Error |
|--------|-----|---------------------|-----------------|-------|
| GET | /offers | | [offer, ...] | |
| GET | /offers/{id} | | offer | (e1) |
| POST | /offers | offer without id | created offer | (e2) |
| PUT | /offers/{id} | updated offer | updated offer | (e1) |
| DELETE | /offers/{id} | | | (e1) |

An offer can be connected to a WorkRequest.

The OfferService can approve or reject an offer.

When an offer is approved, the related WorkRequest is also updated to:

```text
GODKENDT
```

When an offer is rejected, the related WorkRequest is updated to:

```text
AFVIST
```

---

# Bookings

The Booking API is used to manage customer bookings.

| Method | URL | Request Body (JSON) | Response (JSON) | Error |
|--------|-----|---------------------|-----------------|-------|
| GET | /bookings | | [booking, ...] | |
| GET | /bookings/{id} | | booking | (e1) |
| POST | /bookings | booking without id | created booking | (e2) |
| PUT | /bookings/{id} | updated booking | updated booking | (e1) |
| DELETE | /bookings/{id} | | | (e1) |

Example Booking:

```json
{
  "dato": "2026-11-20",
  "tid": "10:30",
  "beskrivelse": "Maling af stue",
  "kundenavn": "Anna",
  "email": "anna@test.dk",
  "telefon": "12345678",
  "serviceIds": []
}
```

A booking date cannot be in the past.

New bookings start with the status:

```text
AFVENTER
```

The BookingService can change the status of a booking.

Possible operations include:

```text
GODKENDT
AFVIST
AFSLUTTET
```

---

# AI Chat – Gemini LLM

The application integrates Gemini as a Large Language Model (LLM).

The chatbot can answer customer questions about DH Maler & Byggeservice and its services.

| Method | URL | Request Body (JSON) | Response (JSON) | Error |
|--------|-----|---------------------|-----------------|-------|
| POST | /chat | question | answer | (e2) |

## Example request

```json
{
  "question": "Kan I hjælpe med VVS arbejde?"
}
```

## Example response

```json
{
  "answer": "Ja, det kan vi bestemt! Vi tilbyder professionelt VVS-arbejde."
}
```

The communication flow is:

```text
Client
  ↓
ChatService
  ↓
GeminiApi
  ↓
Gemini API
```

---

# Geoapify

The application uses Geoapify for address search.

Geoapify is used to find address suggestions based on user input.

The communication flow is:

```text
Client
  ↓
GeoapifyService
  ↓
GeoapifyApi
  ↓
Geoapify REST API
```

The API key is loaded from an environment variable.

Example:

```text
GEOAPIFY_API_KEY
```

---

# Email

The application can send confirmation emails when a customer creates a WorkRequest.

Email sending runs in a separate thread so the HTTP request does not have to wait for the email to be sent.

The flow is:

```text
WorkRequestController
  ↓
EmailTask
  ↓
EmailService
  ↓
Jakarta Mail
  ↓
SMTP
  ↓
smtp.simply.com
```

The application uses the Simply SMTP server.

```text
smtp.simply.com
```

Port:

```text
587
```

The email contains a confirmation that the customer's work request has been received.

---

# Errors

Errors are returned using an appropriate HTTP status code.

## (e1) Resource not found

Example:

```json
{
  "message": "WorkRequest not found"
}
```

HTTP status:

```text
404 Not Found
```

## (e2) Invalid request

Invalid input results in a bad request.

Examples include:

- Missing first name
- Missing last name
- Invalid email
- Invalid phone number
- Missing address
- Missing description

HTTP status:

```text
400 Bad Request
```

## Internal server error

Unexpected server errors return:

```text
500 Internal Server Error
```

---

# HTTP Status Codes

| Status | Meaning |
|--------|---------|
| 200 OK | Request successful |
| 201 Created | Resource created successfully |
| 204 No Content | Resource deleted successfully |
| 400 Bad Request | Invalid request |
| 404 Not Found | Resource not found |
| 500 Internal Server Error | Server error |

---

# Validation

The application validates incoming data before creating resources.

For WorkRequests the following validation rules are used:

| Field | Validation |
|-------|------------|
| fornavn | Must not be empty |
| efternavn | Must not be empty |
| email | Must contain a valid email address |
| telefon | Must contain 8 digits |
| adresse | Must not be empty |
| beskrivelse | Must not be empty |

Example:

```text
Telefonnummer skal bestå af 8 cifre
```

The validation is performed before the WorkRequest is saved in the database.

---

# Logging

The application uses:

```text
SLF4J
Logback
```

SLF4J is used in the Java code for logging.

Logback handles the logging configuration.

The application logs information about HTTP requests and responses.

The logging includes:

- HTTP request method
- Request path
- HTTP response status
- Response time
- Exceptions

Example:

```text
REQUEST: GET /api/workrequests
RESPONSE: GET /api/workrequests | Status: 200 | Time: 25 ms
```

---

# Concurrency

The application uses concurrency when sending confirmation emails.

When a new WorkRequest is created, an EmailTask is started in a separate thread.

Example:

```java
Thread emailThread =
        new Thread(new EmailTask(saved));

emailThread.start();
```

This means the email can be sent independently while the application continues processing other requests.

---

# Database

The application uses:

```text
PostgreSQL
Hibernate
JPA
```

Hibernate/JPA is used for communication between the Java application and the PostgreSQL database.

DAO classes are responsible for database operations.

Examples:

```text
BookingDAO
WorkRequestDAO
OfferDAO
ServiceDAO
```

---

# Architecture

The application is divided into different layers.

```text
Client
  ↓
Routes
  ↓
Controller
  ↓
Service
  ↓
DAO
  ↓
Hibernate / JPA
  ↓
PostgreSQL
```

DTOs are used to transfer data between the API and the client.

Mappers are used to convert between entities and DTOs.

Example:

```text
WorkRequest
      ↓
WorkRequestMapper
      ↓
WorkRequestDTO
```

---

# Testing

The project contains DAO tests, service tests and REST API tests.

The application is tested using:

```text
JUnit
Mockito
REST Assured
Hamcrest
Testcontainers
```

## DAO Tests

DAO tests verify communication with the database.

Examples include:

- Create
- Find by ID
- Find all
- Update
- Update status
- Delete

## Service Tests

Service tests verify the business logic.

Mockito is used to mock dependencies such as DAO classes and external API classes.

Examples include:

```text
BookingServiceTest
ChatServiceTest
OfferServiceTest
```

BookingService tests verify status changes such as:

```text
GODKENDT
AFVIST
AFSLUTTET
```

ChatServiceTest verifies that ChatService communicates correctly with GeminiApi.

OfferServiceTest verifies that approving or rejecting an offer also updates the related WorkRequest.

## REST API Tests

REST Assured and Hamcrest are used to test the REST API.

The WorkRequest API tests cover:

- POST WorkRequest
- GET all WorkRequests
- GET WorkRequest by ID
- PUT WorkRequest status
- DELETE WorkRequest
- 404 Not Found scenario

Example:

```java
given()
        .when()
        .get("/api/workrequests")
        .then()
        .statusCode(200);
```

---

# Technologies

The project uses:

- Java
- Javalin
- Hibernate
- JPA
- PostgreSQL
- Jackson
- SLF4J
- Logback
- Jakarta Mail
- Geoapify API
- Gemini API
- JUnit
- Mockito
- REST Assured
- Hamcrest
- Testcontainers

---

# Summary

DH Maler & Byggeservice is a backend application for handling customer requests and bookings.

The application provides functionality for:

- Work requests
- Offers
- Bookings
- Address search with Geoapify
- AI customer service with Gemini
- Confirmation emails
- Logging
- Validation
- Database persistence
- REST API testing
- DAO testing
- Service testing