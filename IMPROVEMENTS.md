# swissknife

## Overview
Collection of modular, general-purpose libraries with no company-specific dependencies. Provides foundational utilities for distributed systems: core domain types, serialization, messaging, web services, security, database access, logging, and observability.

## Scorecard

| Dimension | Rating | Notes |
|-----------|--------|-------|
| Build system | A | Gradle 9.4.0, 38 version entries, excellent convention plugins |
| Code quality | A | Strong DDD patterns, type safety, value classes, sealed hierarchies |
| Test coverage | B- | 38 test files across 98 modules (~0.4 tests/module) |
| Documentation | D+ | 3-line README, no module docs, no ADRs |
| Dependency freshness | A | All 30+ dependencies current |
| Modularity | A+ | 98 modules, clean dependency graph, no circular deps |
| Maintainability | A- | 13,565 LOC, well-structured, ~20 TODOs |

## Structure
- 98 modules across: core, DDD, serialization, web/HTTP4k, messaging (Pulsar/NATS), SQL/PostgreSQL, cryptography/JWT, logging/OpenTelemetry, testing
- ~13,565 lines of Kotlin

## Issues
- Sparse documentation (no module READMEs, no ADRs, no usage guides)
- Test coverage uneven — some modules lack tests entirely
- Only 1 vulnerable dependency tracked (commons-compress) — could expand
- Configuration cache disabled
- All consumers use SNAPSHOT versioning (no release discipline)

## Potential Improvements
1. Add module-level README files explaining purpose and usage examples
2. Create architecture decision records (ADRs) for major patterns
3. Publish a BOM module for consumers to import consistent versions
4. Expand vulnerable dependency tracking beyond commons-compress
5. Increase test coverage in undertested modules (messaging, SQL adapters)
6. Consider consolidating OpenAPI modules (7 modules for one concern)
7. Move from SNAPSHOT to semantic versioning with release discipline
