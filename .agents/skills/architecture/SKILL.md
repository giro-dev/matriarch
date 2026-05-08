---
name: matriarch-architecture
description: Architecture guide for the Matriarch Object Mother library. Explains the design, key components, and data flow for AI agents working on the codebase.
---

# Matriarch - Architecture Skill

## Design Overview

Matriarch implements the **Object Mother** pattern — a centralized factory for creating test objects with random or specified values. The library uses deep reflection to populate any POJO, record, or complex object graph.

## Core Pipeline

```
Mother.forClass(T.class)
  → Resolve class metadata (Reflection Cache)
  → Detect circular dependencies
  → For each field/parameter:
      1. Check explicit overrides (forField with value)
      2. Check custom field generators (forField with Supplier)
      3. Check type-based generators (forType with Supplier)
      4. Check known patterns (System Props > YAML > built-in)
      5. Fall back to random generation (type-specific generators)
  → Build object via constructor/factory/setters
  → Return populated instance
```

## Key Components

### Domain Core (`domain/core/`)
- **ObjectBuilder** — Main engine that orchestrates object construction
- **ReflectionCache** — Caches class metadata for performance (fields, constructors, methods)
- **CircularDependencyDetector** — Prevents infinite recursion when objects reference each other

### Domain Model (`domain/model/`)
- **Overrider** — Value override instruction with type (STRING, OBJECT, REGEX, NULL)
- **Definition** — Metadata wrapper for a Field or Parameter being populated
- **Coordinate** — Dot-notation path for targeting nested fields (e.g., `user.address.city`)
- **Pattern** — A known pattern mapping a field coordinate to a value generation rule

### Generators (`generators/`)
- Type-specific strategies for generating random data:
  - Primitives (int, long, double, boolean, etc.)
  - Strings (with optional regex patterns)
  - Dates/times (LocalDate, Instant, ZonedDateTime, etc.)
  - Collections (List, Set, Map)
  - Enums
  - Complex objects (recursive generation)

### Infrastructure (`infrastructure/`)
- **PatternRepository** — Aggregates patterns from multiple sources with priority weighting
- **YamlPatternLoader** — Loads known patterns from YAML configuration files
- **SystemPropertyPatternLoader** — Loads patterns from JVM system properties (`-Dmatriarch.pattern.*`)

### Suppliers (`suppliers/`)
Smart data suppliers organized by domain:
- `address/` — City, Country, ZipCode, Street, Coordinates, Latitude, Longitude
- `company/` — CompanyName, JobTitle, Industry, Department
- `datetime/` — Past/Future dates, TimeZone
- `financial/` — Financial data
- `internet/` — Internet-related data
- `numeric/` — Numeric data
- `personal/` — Username, Name
- `text/` — Word, Sentence, Paragraph, LoremIpsum, Title
- `util/` — Boolean, Priority, Status, EnumValue

### JUnit Integration (`junit/`)
- **MotherFactoryResource** — Annotation for `@ParameterizedTest` to auto-generate test arguments
- **RandomArg** — Configures a single generated argument (class, overrides, name)
- **OverrideField** — Field-level override within `@RandomArg`
- Internal processors handle the annotation-to-argument resolution

## Override Precedence

```
Highest priority
  1. .forField("field", value)           — Explicit value
  2. .forField("field", () -> supplier)  — Custom field generator
  3. .forType(Class, () -> supplier)     — Type-based generator
  4. Known patterns (System Props > YAML > built-in)
  5. Random generation                   — Type-specific default
Lowest priority
```

## Object Construction Strategies

Matriarch handles multiple object construction patterns:
1. **Public constructor** — Preferred; parameters matched by name/type
2. **Records** — Constructor parameters mapped to record components
3. **Static factory methods** — Detected and used when no public constructor
4. **Default constructor + setters** — Fallback when no parameterized constructor
5. **Inheritance** — Parent class fields populated recursively

## Thread Safety

- `ReflectionCache` is thread-safe (concurrent map)
- `Mother` builder instances are NOT thread-safe — create new builders per thread
- Suppliers are stateless and thread-safe
