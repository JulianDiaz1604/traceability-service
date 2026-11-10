# traceability-service

Order status history and efficiency metrics — microservice of the **Plazoleta de comidas** challenge (PRAGMA).

## User stories

- HU17 — Order traceability (status history)
- HU18 — Order efficiency metrics

## Stack

Java 21 · Spring Boot 3.5.x · Gradle 8.10.x · DynamoDB (AWS SDK v2 Enhanced Client) · Spring Security + JWT · MapStruct · Lombok · springdoc-openapi · JUnit 5 + Mockito.

> HU17 replaced the JPA archetype with a DynamoDB persistence layer (`infrastructure/out/dynamo`).
> For local development, run DynamoDB Local and point `DYNAMODB_ENDPOINT` at it.

## Architecture

Hexagonal (ports & adapters): `domain` / `application` / `infrastructure`. Full conventions in `CLAUDE.md`.

## Run

```bash
./gradlew clean build
./gradlew bootRun   # http://localhost:8084 - Swagger UI at /swagger-ui.html
```
