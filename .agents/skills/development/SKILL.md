---
name: matriarch-development
description: Development workflow for the Matriarch Object Mother library. Covers building, testing, and working with the codebase.
---

# Matriarch - Development Skill

## Prerequisites

- Java 21 (Eclipse Temurin recommended)
- Maven (via included wrapper `./mvnw`)

## Build & Test

```bash
# Full build with tests
./mvnw clean install

# Tests only
./mvnw test

# Build without tests
./mvnw clean install -DskipTests

# Package JAR
./mvnw package
```

## Project Layout

| Directory | Purpose |
|-----------|---------|
| `src/main/java/dev/agiro/matriarch/domain/core/` | Core generation logic and reflection cache |
| `src/main/java/dev/agiro/matriarch/domain/model/` | Domain entities (Overrider, Definition, Coordinate) |
| `src/main/java/dev/agiro/matriarch/generators/` | Type-specific data generation strategies |
| `src/main/java/dev/agiro/matriarch/infrastructure/` | Pattern loading (YAML, System Properties) |
| `src/main/java/dev/agiro/matriarch/junit/` | JUnit 5 extensions and annotations |
| `src/main/java/dev/agiro/matriarch/suppliers/` | Smart data suppliers by category |
| `src/main/java/dev/agiro/matriarch/util/` | TypeReference and utilities |
| `src/test/java/dev/agiro/matriarch/examples/` | Usage examples and integration tests |
| `src/test/java/dev/agiro/matriarch/object_samples/` | Test POJOs (records, inheritance, generics) |
| `website/` | Hugo documentation site |

## Common Tasks

### Adding a new Supplier
1. Choose the appropriate category subpackage under `suppliers/` (address, company, datetime, etc.)
2. Create a new class extending the base supplier
3. Implement the data generation logic
4. Register the supplier in the known patterns if it should be auto-matched by field name

### Adding a new Override type
1. Add the type to the `Overrider.OverriderType` enum in `domain/model/`
2. Handle the new type in the core generation logic in `domain/core/`
3. Add test coverage in `src/test/`

### Modifying the Builder API
1. Update the `Mother` class builder methods
2. Maintain backward compatibility — keep old method signatures as aliases
3. Update the README.md with new usage examples
4. Add test coverage for the new API surface

### Working with JUnit 5 annotations
- `@MotherFactoryResource` — Top-level annotation for parameterized tests
- `@RandomArg` — Defines a single test case with target class and overrides
- `@OverrideField` — Field-level override (value, regex, null)
- Annotation processors are in `junit/annotations/internal/`

## Release Process
- Releases publish to Maven Central via Sonatype
- GPG signing is required (`maven-gpg-plugin`)
- CI/CD runs via GitHub Actions (`.github/workflows/release.yml`)
- Version is managed in `pom.xml` `<version>` tag

## Important Constraints
- **No Lombok** — This is a published library; avoid external annotation processors
- **Minimal dependencies** — Every new dependency increases the transitive footprint for consumers
- **Backward compatibility** — Never remove or change public API signatures without deprecation
- **API stability** — This library is on Maven Central; breaking changes require major version bumps
