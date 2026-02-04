# Pet Managing API

Simple Spring Boot REST API to manage pets (in-memory repository).

## Tech Stack

- Java 21
- Spring Boot 3
- Maven
- JUnit 5 / Mockito / MockMvc
- Log4j2

## Run the App

```bash
./mvnw spring-boot:run
```

Default URL: `http://localhost:8080`

## Testing

### Unit tests

```bash
./mvnw test
```

### Integration tests (`*IT`, Failsafe)

```bash
./mvnw verify
```

## Bruno Requests

Open `requests/` as a Bruno collection.

- Environment: `requests/environments/local.bru`
- Includes CRUD, validation, malformed JSON, and not-found scenarios

## Notes

- Data is stored in memory (`MockPetRepositoryImpl`), so it resets on restart.
- Logging is enabled across controller, service, repository, and exception handler.
