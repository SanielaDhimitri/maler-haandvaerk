## API Documentation

The application provides a REST API built with Javalin.

Base URL:

http://localhost:7072/api

### Work Requests

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /workrequests | Get all work requests |
| GET | /workrequests/{id} | Get a work request by ID |
| POST | /workrequests | Create a new work request |
| PUT | /workrequests/{id}/status | Update work request status |
| DELETE | /workrequests/{id} | Delete a work request |

### Offers

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /offers | Get all offers |
| GET | /offers/{id} | Get an offer by ID |
| POST | /offers | Create a new offer |
| PUT | /offers/{id} | Update an offer |
| DELETE | /offers/{id} | Delete an offer |

### Bookings

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /bookings | Get all bookings |
| GET | /bookings/{id} | Get a booking by ID |
| POST | /bookings | Create a new booking |
| PUT | /bookings/{id} | Update a booking |
| DELETE | /bookings/{id} | Delete a booking |

## HTTP Status Codes

| Status | Meaning |
|--------|---------|
| 200 OK | Request successful |
| 201 Created | Resource created successfully |
| 204 No Content | Resource deleted successfully |
| 400 Bad Request | Invalid request |
| 404 Not Found | Resource not found |
| 500 Internal Server Error | Server error |

## Logging

The application uses SLF4J and Logback for logging.

The application logs:

- HTTP request method
- Request path
- Request body
- HTTP response status
- Response time
- Exceptions

Logs are shown in the console and saved in:

`logs/app.log`

## API Testing

The REST API is tested using:

- JUnit
- REST Assured
- Hamcrest

API tests cover:

- POST
- GET
- PUT
- DELETE
- 404 Not Found scenarios