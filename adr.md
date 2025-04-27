---
# Architecture Decision Record (ADR) 001: Use of Spring Boot Framework

## Status
Proposed

## Context and Problem
The project requires a scalable, maintainable Java backend with REST APIs, database integration, monitoring, and ease of deployment. A modern Java framework is needed that facilitates rapid development and future scalability.

## Decision
Adopt Spring Boot as the primary application framework for REST API development, database interactions (via Spring Data JPA), metrics, and health management (via Spring Boot Actuator).

## Justification
Spring Boot offers rapid development with convention over configuration, a large ecosystem, and support for microservices architecture. It integrates well with monitoring solutions, OpenTelemetry, and background job processing.

## Consequences
- Developers must be proficient with Spring Boot.
- Easy integration of new features and observability.
- Slight overhead of dependency management and learning curve.

---

# Architecture Decision Record (ADR) 002: Domain-Driven Design (DDD) and Clean Architecture

## Status
Proposed

## Context and Problem
The application involves multiple distinct domains (Owner, Toilet, Customer) with complex business rules. Managing complexity and ensuring clear separation of concerns is crucial.

## Decision
Use Domain-Driven Design (DDD) principles and Clean Architecture to organize code into bounded contexts with domain models, use cases, repositories, and infrastructure layers.

## Justification
DDD allows encapsulating core business logic and preserving domain integrity. Clean Architecture enforces dependency rule, facilitating testability and adaptability.

## Consequences
- Complexity in learning DDD concepts.
- Better maintainability and scalability of codebase.
- Clear boundaries simplify future refactoring or splitting into microservices.

---

# Architecture Decision Record (ADR) 003: Event-Driven Architecture with Domain Events

## Status
Proposed

## Context and Problem
Decoupling components and asynchronous processing is needed to enhance scalability and responsiveness (e.g., toilet creation triggers other workflows).

## Decision
Implement event-driven architecture using domain events (ToiletCreatedEvent, ItemCreatedEvent) with event publishers and listeners.

## Justification
Events facilitate loose coupling among bounded contexts and support asynchronous and eventual consistency patterns.

## Consequences
- Increased complexity in event handling.
- Improved system responsiveness and modularity.
- Need monitoring to trace events end-to-end.

---

# Architecture Decision Record (ADR) 004: Background Job Processing with JobRunr

## Status
Proposed

## Context and Problem
Certain tasks such as processing outbox messages or recurring jobs must happen asynchronously and reliably.

## Decision
Use JobRunr for background job processing and scheduling recurring jobs (e.g., ToiletOutboxRecurringJob).

## Justification
JobRunr offers easy Java API for defining and scheduling persistent background jobs that survive application restarts.

## Consequences
- Dependency on an additional library and persistence layer.
- Improved task reliability and offloading from main request threads.

---

# Architecture Decision Record (ADR) 005: Observability Stack Using OpenTelemetry, Prometheus, and EFK

## Status
Proposed

## Context and Problem
Effective monitoring and logging are needed to maintain system health and troubleshoot issues.

## Decision
Adopt OpenTelemetry for distributed tracing and metrics collection, Prometheus for metrics storage and alerting, and EFK stack (Elasticsearch, Fluentd, Kibana) for logging.

## Justification
This combination provides end-to-end observability covering logs, metrics, and traces suitable for Kubernetes environments.

## Consequences
- Additional infrastructure complexity.
- Enhanced ability to diagnose production issues.

---

# Architecture Decision Record (ADR) 006: Containerization and Kubernetes-based Deployment with Helm and GitOps

## Status
Proposed

## Context and Problem
The application needs containerized deployment with automated updates and scalability.

## Decision
Use Docker for container images, Kubernetes for orchestration, Helm charts for templated deployments, and ArgoCD for GitOps-based deployment automation.

## Justification
This stack supports declarative infrastructure, automated continuous deployment, and scalability.

## Consequences
- Needs Kubernetes cluster setup and maintenance.
- DevOps expertise required for GitOps workflows.

---

These ADRs collectively document key architectural decisions and rationale observed from the project analysis. The existing ADR 001 covers the initial framework foundation. The additional ADRs complement it by capturing domain modeling, event-driven design, asynchronous processing, observability, and deployment strategies critical to this project.

# Summary
- Existing ADR 001 confirms Spring Boot as the framework.
- Additional ADRs suggested for DDD, event-driven, async jobs, observability, and containerized deployment.
- These ADRs support project alignment and clarity for future maintenance and enhancements.