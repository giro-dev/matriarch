# Matriarch - Agent Instructions

> Cross-tool agent configuration. Read by Codex, Copilot, Cursor, Windsurf, Devin, and other AI agents.

## Project Overview

Matriarch is a Java library implementing the **Object Mother** pattern for test data generation. It automatically populates POJOs with random data while providing granular control via a fluent Builder API, custom generators, and JUnit 5 integration. Published to Maven Central under `dev.agiro:matriarch`.

## Tech Stack

- **Language:** Java 21
- **Build:** Maven (with Maven Wrapper `mvnw`)
- **Testing:** JUnit 5 (Jupiter)
- **Dependencies:** Jackson (JSON/YAML), SnakeYAML, Reflections
- **Publishing:** Maven Central via Sonatype (GPG-signed)
- **Documentation:** Hugo-based website (`website/`)
- **CI/CD:** GitHub Actions (release, docs, Hugo site)

## Project Structure

```
src/main/java/dev/agiro/matriarch/
  domain/
    core/            # Core generation logic, reflection cache, object building
    model/           # Domain entities: Overrider, Definition, Coordinate, Pattern
    exception/       # Custom exceptions
    exceptions/      # Additional exception types
  generators/        # Type-specific data generation strategies (regex, etc.)
  infrastructure/    # Pattern loading from YAML and System Properties
  junit/
    annotations/     # JUnit 5 annotations (@MotherFactoryResource, @RandomArg, @OverrideField)
      internal/      # Internal annotation processors
  suppliers/         # Smart data suppliers organized by category
    address/         # City, Country, ZipCode, Street, Coordinates, etc.
    company/         # CompanyName, JobTitle, Industry, Department, etc.
    datetime/        # Past/Future dates, TimeZone, etc.
    financial/       # Financial data suppliers
    internet/        # Internet-related data suppliers
    numeric/         # Numeric data suppliers
    personal/        # Username, Name, etc.
    text/            # Word, Sentence, Paragraph, LoremIpsum, Title, etc.
    util/            # Boolean, Priority, Status, EnumValue, etc.
    base/            # Base supplier classes
  util/              # TypeReference and other utilities

src/test/java/dev/agiro/matriarch/
  examples/          # Usage examples and integration tests
  suppliers/         # Supplier tests
  object_samples/    # Test POJOs (records, inheritance, generics, static factory)

website/             # Hugo documentation site
documentation/       # Static docs and images
```

## Build & Test Commands

```bash
./mvnw clean install           # Build and run tests
./mvnw test                    # Run tests only
./mvnw clean install -DskipTests  # Build without tests
./mvnw package                 # Package JAR
```

## Key Concepts

- **Mother:** Entry point class. `Mother.forClass(T.class)` starts the builder chain.
- **Overrider:** Instruction to override a field's random value. Types: `STRING`, `OBJECT`, `REGEX`, `NULL`.
- **Coordinate:** Dot-notation path targeting nested fields (e.g., `user.address.city`).
- **Definition:** Metadata wrapper for reflective Field or Parameter analysis.
- **Known Patterns:** Registry of field-name-to-regex mappings for smart defaults (YAML, System Properties, or built-in).
- **Supplier:** Functional interface providing discrete data points (email, name, city, etc.).
- **Circular Dependency Detector:** Prevents infinite recursion in object graphs.
- **Reflection Cache:** Performance optimizer for class metadata and member lookups.
- **Litter:** A collection of generated objects (`buildList()`, `buildSet()`, `buildStream()`).
- **TypeReference:** Utility to capture and resolve generic type information at runtime.

## Coding Conventions

- No Lombok — this is a library with no external annotation processors.
- Use standard Java patterns: explicit getters/setters, constructors, builders.
- Java records are used in test samples but the library itself supports generating any POJO type.
- Suppliers are organized by domain category in `suppliers/` subpackages.
- Each supplier extends a base class and provides a single responsibility.
- Known patterns use a priority system: System Properties > YAML config > built-in defaults.
- The `Overrider` class is the central mechanism for field value customization.
- `forField()` is the preferred API method; `override()` is kept as a legacy alias.

## API Usage Patterns

### Builder (recommended)
```java
var obj = Mother.forClass(SomeType.class)
    .forField("fieldName", "value")                          // String override
    .forField("nested.field", Overrider.regex("A\\d{4}"))    // Regex override
    .forField("date", Instant.now())                         // Object override
    .forField("optional", Overrider.nullValue())             // Null override
    .forField("email", () -> generateEmail())                // Supplier override
    .forType(LocalDate.class, () -> LocalDate.now())         // Type-based override
    .withCollectionSize(3, 10)                               // Collection size control
    .excludeFields("secret", "internal")                     // Exclude fields
    .build();
```

### Collections
```java
List<User>     list   = Mother.forClass(User.class).buildList(10);
Set<User>      set    = Mother.forClass(User.class).buildSet(10);
Stream<User>   stream = Mother.forClass(User.class).buildStream(10);
```

### JUnit 5 Annotations
```java
@ParameterizedTest
@MotherFactoryResource(args = {
    @RandomArg(name = "Test Case", targetClass = User.class,
        overrides = @OverrideField(field = "email", value = "[a-z]+@test.com", isRegex = true))
})
void test(User user) { /* ... */ }
```

## Override Precedence (highest to lowest)
1. `.forField(field, value)` — Explicit field value
2. `.forField(field, supplier)` — Custom field generator
3. `.forType(class, supplier)` — Type-based generator
4. Pattern matching (YAML / System Properties / default)
5. Random generation

## Important Notes

- This is a **library** published to Maven Central — API stability matters.
- Do not add dependencies without careful consideration; keep the dependency footprint small.
- All public API changes should maintain backward compatibility (e.g., `override()` kept as alias for `forField()`).
- GPG signing is required for release (`maven-gpg-plugin`).
- The `website/` directory contains a Hugo site — it is built separately via GitHub Actions.
- Tests in `object_samples/` contain intentionally varied POJO structures (records, inheritance, generics, static factories) to test edge cases.
