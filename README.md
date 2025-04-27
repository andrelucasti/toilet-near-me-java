# Toilet Near Me Java

## Architecture Overview

```mermaid
flowchart TD
    subgraph Application
        direction TB
        Owner[Owner Domain]
        Toilet[Toilet Domain]
        Customer[Customer Domain]
        Common[Common Modules]
    end

    subgraph Infrastructure
        direction TB
        JPA[Spring Data JPA]
        PostgreSQL[PostgreSQL DB]
        JobRunr[Background Jobs (JobRunr)]
        OpenTelemetry[OpenTelemetry & Prometheus]
        Kong[Kong API Gateway]
        EFK[EFK Logging Stack]
        ArgoCD[GitOps Deployment (ArgoCD)]
        Kubernetes[Kubernetes]
        Docker[Docker Containers]
    end

    Owner -->|Uses| JPA
    Toilet -->|Uses| JPA
    Customer -->|Uses| JPA
    Application -->|Deployed on| Kubernetes
    Kubernetes --> Docker
    Application -->|Background Tasks| JobRunr
    Application -->|Observability| OpenTelemetry
    Kubernetes -->|Ingress| Kong
    Kubernetes -->|Logging| EFK
    Kubernetes -->|Configured by| ArgoCD
    JPA --> PostgreSQL
```

This architecture follows Domain-Driven Design principles, Clean Architecture, and event-driven patterns to ensure maintainability, scalability, and observability.

## Tech Stack

- Java 17/11 (Gradle build)
- Spring Boot 3.3.0
- Spring Data JPA with Hibernate
- PostgreSQL database
- OpenTelemetry & Micrometer (Prometheus compatible)
- JobRunr for background job processing
- Kong API Gateway
- Kubernetes (Helm charts, manifests)
- Docker & Docker Compose
- Flyway for DB migrations
- EFK Stack (Elasticsearch, Fluentd, Kibana)
- ArgoCD for GitOps deployment
- JUnit, Mockito, AssertJ, RestAssured for testing
- JMH (Java Microbenchmark Harness) for performance benchmarking
- k6 for load testing scripts (written in JS)

## Endpoints

| Name                  | Description                        | Request                          | Response                         |
|-----------------------|----------------------------------|---------------------------------|---------------------------------|
| Create Owner          | Registers a new owner             | POST `/owners`                   | Owner details or error           |
| Create Toilet         | Registers a toilet                | POST `/toilets`                  | Toilet details or error          |
| Add New Item          | Adds an item to a toilet          | POST `/toilets/{id}/items`       | Item details or error            |
| Get Toilet Details    | Retrieves details of a toilet     | GET `/toilets/{id}`              | Toilet & items details           |
| Create Customer       | Registers a new customer          | POST `/customers`                | Customer details or error        |
| Other endpoints       | ...                              | See OpenAPI spec (`api-docs.yaml`) | See OpenAPI spec               |

*Refer to `api-docs.yaml` and `openapi.spectral.yaml` for full API specification.*

## Topics

| Name                  | Purpose                          |
|-----------------------|---------------------------------|
| Domain Events         | Asynchronous domain event handling |
| Command Handlers      | Command processing for business actions |

## Queues

| Name                  | Purpose                          |
|-----------------------|---------------------------------|
| Background Jobs Queue | JobRunr recurring jobs and async processing |

## Features

| Feature                | Description                                  |
|------------------------|----------------------------------------------|
| Domain-Driven Design   | Clear separation of bounded contexts (Owner, Toilet, Customer) |
| Event-Driven Architecture | Use of domain events, publishers, and listeners |
| Repository Pattern     | Interface-based repositories with JPA implementation |
| Background Processing  | JobRunr recurring jobs implementing outbox pattern |
| Configuration Profiles | Multiple Spring profiles (dev, test, k8s) |
| Observability          | OpenTelemetry tracing with Prometheus metrics; EFK logging |
| GitOps Deployment      | Kubernetes deployments managed declaratively with ArgoCD |
| API Gateway            | Rate limiting and ingress management with Kong |
| Automated Testing      | Integration, unit, and performance tests; load testing scripts |
| Database Migrations    | Version-controlled database schema with Flyway |

## License

This project is licensed under [Appropriate License Name here, e.g. MIT License].

---

*This README is generated to help developers quickly understand and onboard the Toilet Near Me Java project.*
