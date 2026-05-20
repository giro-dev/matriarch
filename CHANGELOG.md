# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- **CI workflow** for pull requests — runs `mvn verify` on every PR and push to main.
- **Deterministic seeds** — `Mother.forClass(Foo.class).withSeed(42).build()` produces
  reproducible objects. All generators now use a shared `Random` from `GenerationContext`.
- **Strict mode** — `Mother.forClass(Foo.class).strict().build()` throws on generation
  failures instead of silently returning null fields.
- **Debug mode** — `Mother.forClass(Foo.class).debug().build()` logs the full generation
  tree at INFO level for diagnostics.
- **Bean Validation awareness** — when `jakarta.validation-api` is on the classpath,
  generated values respect `@NotNull`, `@NotBlank`, `@Size`, `@Min`, `@Max`, `@Pattern`,
  `@Email`, `@Positive`, `@Negative`, `@DecimalMin`, `@DecimalMax`, and more.
- **JPMS support** — `Automatic-Module-Name: dev.agiro.matriarch` in the JAR manifest.
- **CHANGELOG.md** — this file.
- `GenerationContext` — thread-local context that carries configuration (collection sizes,
  random seed, strict/debug flags) through the generation pipeline.

### Fixed
- **`withCollectionSize` now works** — collection size configuration is wired through
  `GenerationContext` to `ListGenerator`, `SetGenerator`, `ArrayGenerator`, and `MapGenerator`.
  Previously, the Javadoc stated it was "not applied during object generation."
- **ObjectMotherGenerator TypeReference inconsistency** — `Builder.build()` now reuses
  the existing `ObjectMotherGenerator` instance instead of creating a new one when using
  `TypeReference`.
- **Hugo docs workflow path filter** — changed broken `../../website_/**` to `website/**`.
- **README version inconsistencies** — Maven example updated from `1.0.0` to `1.1.1`,
  Gradle example updated from `0.1.0` to `1.1.1`.

### Changed
- **JUnit dependency versions aligned** — both `junit-jupiter` and `junit-jupiter-params`
  now use version `5.11.4` (previously mismatched `5.10.1` / `5.9.2`).
- **maven-surefire-plugin** updated from `2.22.2` to `3.5.2`.
- All generators now use `GenerationContext.getInstance().getRandom()` instead of creating
  `new SecureRandom()` on every invocation, enabling deterministic generation and improving
  performance.

## [1.1.1] - 2024-12-XX

### Fixed
- Initial stable release with Object Mother pattern support.

[Unreleased]: https://github.com/giro-dev/matriarch/compare/v1.1.1...HEAD
[1.1.1]: https://github.com/giro-dev/matriarch/releases/tag/v1.1.1
