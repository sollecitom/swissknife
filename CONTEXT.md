# Swissknife - Project Context

## Purpose and Scope

Swissknife is a collection of modular, general-purpose Kotlin libraries for building distributed systems. It provides foundational utilities covering core domain types, serialization, messaging, web services, security, database access, logging, and observability. Nothing company-specific belongs here -- that goes in **pillar** (a separate repository that depends on swissknife and adds company-specific adapters, configurations, and conventions).

## Module Organization

All modules live under `modules/` and are organized by concern area. Each module is a Gradle subproject registered in `settings.gradle.kts` using the `module(...)` helper function. Module names follow the pattern `{area}-{sub-area}-{role}`, e.g., `pulsar-messaging-adapter` or `sql-postgres-container`.

### Module Categories

| Area | Description |
|------|-------------|
| `core/` | Foundational domain types (Id, Name, Currency, Port, etc.), utility generators, and test utilities |
| `kotlin/` | Pure Kotlin extension functions (collections, coroutines, encoding, time, text, etc.) |
| `ddd/` | Domain-Driven Design building blocks (Event, Command, Query, Entity, hexagonal adapter interfaces) |
| `correlation/` | Invocation context, tracing, access/actor modeling, tenant isolation, toggles |
| `json/` | JSON serialization/deserialization utilities with schema validation and compliance rules |
| `avro/` | Avro serialization, schema repository, schema catalogue, and compliance checking |
| `messaging/` | Messaging abstractions (Message, Producer, Consumer, Topic, partitioning) |
| `pulsar/` | Apache Pulsar adapter for messaging (JSON and Avro serialization, client utils) |
| `nats/` | NATS client adapter (publisher, consumer) |
| `web/` | Web API domain (endpoints, errors), HTTP driving adapter utilities, client-info analysis, OpenAPI integration |
| `openapi/` | OpenAPI specification parsing, building, providing, compliance checking, and request/response validation |
| `logger/` | Custom logging framework (core engine, SLF4J adapter, JSON formatter) |
| `logging/` | Standard logging configuration utilities |
| `jwt/` | JWT domain types and jose4j-based processing/issuing |
| `cryptography/` | Symmetric encryption domain and Bouncy Castle implementation |
| `sql/` | SQL connection options, reactive utilities, Liquibase migration, PostgreSQL adapters and containers |
| `http4k/` | Http4k utility extensions (lenses, headers, server wrappers) |
| `lens/` | Http4k lens extensions for core domain types and correlation context |
| `configuration/` | Configuration loading via Http4k Environment (YAML, JVM properties, env vars) |
| `pagination/` | Cursor-based pagination domain model |
| `serialization/` | Generic Serializer/Deserializer interfaces |
| `protected-value/` | Encrypted value protection with AES-CTR implementation |
| `hashing/` | Hash function abstractions and Blake3 implementation |
| `readiness/` | Readiness check domain (health checking) |
| `service/` | Service identity/lifecycle and HTTP readiness adapters |
| `compliance-checker/` | Generic compliance rule engine for checking any target against rule sets |
| `test/` | Shared test utilities (assertions, parameterized tests, standard output capture) and Testcontainers support |
| `keycloak/` | Keycloak Testcontainer for integration testing |
| `opentelemetry/` | OpenTelemetry SDK integration, OTLP exporter, Grafana test stack |
| `resource/` | Classpath resource loading utilities |

### Module Naming Conventions

Modules follow a layered naming pattern:
- **`domain`** -- Pure domain types and interfaces (no implementation dependencies)
- **`utils`** -- Utility functions and helpers, typically depending on a specific library
- **`adapter`** -- Adapter implementing domain interfaces with a specific technology
- **`test/utils`** -- Test factories, assertions, and specifications for a given area
- **`test/specification`** -- Reusable test specifications (behavior contracts)
- **`container`** -- Testcontainer setup for a specific technology

## Key Architectural Patterns

### Hexagonal Architecture
The project enforces port-and-adapter separation. Domain modules define interfaces (ports), while adapter modules provide implementations. See `ddd/domain` for `DrivenAdapter` and `DrivingAdapter` marker interfaces.

### Domain-Driven Design (DDD)
Rich domain modeling with:
- **Events** with metadata, context, and lineage tracking (`Event`, `Event.Context`, `Event.Reference`)
- **Commands** and **Queries** as distinct instruction types
- **Entities** identified by `Id`
- **Value objects** using Kotlin value classes and data classes

### Invocation Context
A cross-cutting concern carried through every operation, containing:
- Trace information (invocation ID, action ID, originating invocation)
- Access information (actor, tenant, authentication, authorization)
- Toggle values for feature flags

### Compliance Checking
A generic pattern used across the codebase: define `ComplianceRule` instances, group them into `ComplianceRuleSet`, and run them via `ComplianceChecker`. Applied to JSON schemas, Avro schemas, and OpenAPI specifications.

### Test Specifications
Reusable test contracts defined as interfaces with default test methods. Implementing classes only need to provide the fixture. Examples: `JsonSerdeTestSpecification`, `AvroSerdeTestSpecification`, `MessagingTestSpecification`, `EqualsAndHashCodeTestSpecification`.

## Dependency Conventions

### api vs implementation
- Use `api` when the dependency's types appear in the module's public API (interfaces, function signatures)
- Use `implementation` when the dependency is only used internally
- Domain modules typically `api`-expose other domain modules
- Adapter/utils modules `implementation`-depend on the libraries they wrap

### How to Add a New Module
1. Create the directory structure: `modules/{area}/{sub-area}/{role}/`
2. Add a `build.gradle.kts` with the appropriate convention plugins
3. Register it in `settings.gradle.kts` using `module("area", "sub-area", "role")`
4. Follow the `domain`/`utils`/`adapter`/`test-utils` layering pattern
5. Typical build file:
   ```kotlin
   plugins {
       id("sollecitom.kotlin-library-conventions")
       id("sollecitom.maven-publish-conventions")
   }
   dependencies {
       api(projects.coreDomain)
       testImplementation(projects.testUtils)
   }
   ```

### Convention Plugins
Convention plugins are defined in the external `../gradle-plugins` repository and applied via `includeBuild`. Key conventions:
- `sollecitom.kotlin-library-conventions` -- Standard Kotlin/JVM library setup
- `sollecitom.maven-publish-conventions` -- Maven Local publishing
- `sollecitom.dependency-update-conventions` -- Dependency update checking
- `sollecitom.aggregate-test-metrics-conventions` -- Test metrics aggregation

### Version Catalog
All dependency versions are centralized in `gradle/libs.versions.toml`. Use `libs.{library-name}` for dependencies and `projects.{moduleName}` for inter-module dependencies (enabled by `TYPESAFE_PROJECT_ACCESSORS`).

## Testing Conventions

### Frameworks
- **JUnit Jupiter** for test execution
- **assertk** for fluent assertions
- **kotlinx-coroutines-test** for coroutine testing
- **Testcontainers** for integration tests (Pulsar, PostgreSQL, Keycloak, Grafana OTEL)

### Style
- Test classes use `@TestInstance(PER_CLASS)`
- Test method names use backtick-quoted descriptive strings: `` `creating a value from a factory` ``
- Tests are organized using the test specification pattern -- reusable interfaces defining behavior contracts
- Test utility modules provide factories, assertions, and stubs for their parent domain

### Running Tests
```bash
./gradlew build          # Build and test everything
./gradlew :module-name:test  # Test a specific module
just build               # Shortcut via justfile
```

## Build System

- **Gradle 9.4.0** with Kotlin DSL
- **Kotlin 2.3.20** targeting JVM
- Version management via `libs.versions.toml` (version catalog)
- Typesafe project accessors enabled (`TYPESAFE_PROJECT_ACCESSORS`)
- Git-based versioning via Palantir plugin (currently SNAPSHOT)
- Publishing to Maven Local via `./gradlew publishToMavenLocal` or `just publish`

### Key Build Commands
| Command | Description |
|---------|-------------|
| `just build` | Build and test everything |
| `just rebuild` | Clean build with dependency refresh |
| `just publish` | Publish to Maven Local |
| `just update-dependencies` | Check for dependency updates |
| `just update-gradle` | Update Gradle wrapper to latest |

## Swissknife vs Pillar

| Aspect | Swissknife | Pillar |
|--------|-----------|--------|
| Purpose | General-purpose libraries | Company-specific adapters and conventions |
| Dependencies | Open-source only | Depends on swissknife + internal systems |
| Examples | Messaging abstractions, JWT processing, SQL utilities | Company auth integration, specific service templates |
| Rule | If it could be open-sourced, it belongs in swissknife | If it references internal systems, it belongs in pillar |
