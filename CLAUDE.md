# Matriarch - Claude Code Instructions

> For project overview and full conventions, see [AGENTS.md](./AGENTS.md).

## Quick Reference

- **Language:** Java 21 (no Lombok — this is a published library)
- **Build:** Maven (`./mvnw`)
- **Published to:** Maven Central (`dev.agiro:matriarch`)
- **Testing:** JUnit 5

## Commands

```bash
./mvnw clean install    # Build + test
./mvnw test             # Tests only
./mvnw package          # Package JAR
```

## Key Patterns

- Entry point: `Mother.forClass(T.class)` → fluent builder → `.build()`
- Override fields: `.forField("coord.path", value)` (dot-notation for nesting)
- Override types: STRING, OBJECT, REGEX, NULL
- Custom generators: `.forField("f", () -> supplier)` and `.forType(Class, () -> supplier)`
- Collections: `.buildList(n)`, `.buildSet(n)`, `.buildStream(n)`
- JUnit 5: `@MotherFactoryResource` + `@RandomArg` + `@OverrideField`
- Suppliers organized by domain in `suppliers/` subpackages
- No external annotation processors — keep dependency footprint minimal
- `forField()` is preferred; `override()` is legacy alias
- Maintain backward compatibility for all public API changes
