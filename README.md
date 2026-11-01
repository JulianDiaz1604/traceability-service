# traceability-service

Order status history and efficiency metrics — microservice of the **Plazoleta de comidas** challenge (PRAGMA PowerUp).

## User stories

- HU17 — Order traceability (status history)
- HU18 — Order efficiency metrics

## Stack

Java 21 · Spring Boot 3.5.x · Gradle 8.10.x · MongoDB · MapStruct · Lombok · springdoc-openapi · JUnit 5 + Mockito.

> Scaffolded from the shared JPA archetype; migrate persistence to MongoDB when implementing HU17/HU18.

## Architecture

Hexagonal (ports & adapters): `domain` / `application` / `infrastructure`. Full conventions in `CLAUDE.md`.

## Run

```bash
./gradlew clean build
./gradlew bootRun   # http://localhost:8084 - Swagger UI at /swagger-ui.html
```
