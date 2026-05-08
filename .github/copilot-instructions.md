# Matriarch - Copilot Instructions

> For project overview and full conventions, see [AGENTS.md](../AGENTS.md) in the project root.

## Tech Stack
- **Language:** Java 21 (no Lombok — this is a published library)
- **Build:** Maven (`./mvnw`)
- **Published to:** Maven Central (`dev.agiro:matriarch`)
- **Testing:** JUnit 5
- **Dependencies:** Jackson, SnakeYAML, Reflections

## Conventions
- Entry point: `Mother.forClass(T.class)` starts the fluent builder chain.
- Use `.forField("coordinate", value)` for field overrides (dot-notation for nesting).
- Override types: `STRING`, `OBJECT`, `REGEX`, `NULL`.
- Custom generators: `.forField("field", () -> supplier)` and `.forType(Class, () -> supplier)`.
- Collections: `.buildList(n)`, `.buildSet(n)`, `.buildStream(n)`.
- JUnit 5 integration: `@MotherFactoryResource`, `@RandomArg`, `@OverrideField`.
- Suppliers are organized by domain category in `suppliers/` subpackages.
- No external annotation processors — keep the dependency footprint minimal.
- `forField()` is the preferred API; `override()` is kept as a legacy alias.
- Maintain backward compatibility for all public API changes.
- This is a library — API stability matters. Do not add dependencies without careful consideration.

## Build Commands
```bash
./mvnw clean install    # Build + test
./mvnw test             # Tests only
./mvnw package          # Package JAR
```
