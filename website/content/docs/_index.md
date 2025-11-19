---
title: "Matriarch Documentation"
date: 2025-11-15
draft: false
weight: 1
---

# Matriarch Documentation

Welcome to the **Matriarch** documentation! Matriarch is a powerful ObjectMother library designed to simplify test data
generation in Java applications.

## What is Matriarch?

Matriarch helps your testing process by generating objects with random values in an easy way, giving you the flexibility
to force specific values into defined fields. It's lightweight, fast, and integrates seamlessly with JUnit.

## Key Features

✨ **Flexible Object Generation** - Create fully populated test objects with random or specified values  
🎯 **Smart Overrides** - Override specific fields using dot notation for nested objects  
🔤 **Regex Pattern Support** - Generate data matching regex patterns with built-in generator  
📝 **Builder Pattern** - Fluent, readable API for test data creation  
🧪 **JUnit Integration** - Seamless integration with JUnit ParameterizedTest  
📊 **Named Test Cases** - Add descriptive names for better test reporting  
🎲 **Multiple Strategies** - Use as builder, factory, or JUnit annotation  
⚡ **Lightweight** - No heavy dependencies, fast test execution  
🧩 **Custom Field Generators** - `.forField("field", Supplier<?>)` for per-field custom values  
🧱 **Type-based Generators** - `.forType(Class<T>, Supplier<T>)` to affect all fields of a type  
🧭 **Known Patterns** - Built-in defaults, YAML overrides, and JVM system properties support

## Quick Navigation

- **[Getting Started]({{< ref "getting-started.md" >}})** - Installation and first steps
- **[Core Concepts]({{< ref "guide/core-concepts.md" >}})** - Understand how Matriarch works
- **[Builder API]({{< ref "guide/builder-api.md" >}})** - Detailed builder pattern usage
- **[Advanced Usage]({{< ref "guide/advanced-usage.md" >}})** - Custom generators and complex scenarios
- **[API Reference]({{< ref "guide/api-reference.md" >}})** - Complete API documentation
- **[JUnit Integration]({{< ref "junit-integration.md" >}})** - Parameterized tests and field injection
- **[Pattern System]({{< ref "pattern-system.md" >}})** - Known patterns and custom patterns
- **[Built-in Suppliers]({{< ref "suppliers.md" >}})** - 43 ready-to-use data generators
- **[Examples]({{< ref "examples.md" >}})** - Real-world usage examples

## Quick Example

```java
// Generate a random user
User user = Mother.forClass(User.class).build();

// Generate with specific values
User admin = Mother.forClass(User.class)
        .forField("email", "admin@example.com")
        .forField("role", "ADMIN")
        .forField("age", 30)
        .build();

// Generate with regex patterns
User user = Mother.forClass(User.class)
        .forField("email", new Regex("[a-z]{5,10}@gmail.com"))
        .forField("phoneNumber", new Regex("\\d{3}-\\d{3}-\\d{4}"))
        .build();

// Collections
List<User> users = Mother.forClass(User.class).buildList(10);

// Use in parameterized tests
@ParameterizedTest
@MotherFactoryResource(args = {
        @RandomArg(
                name = "Valid User",
                targetClass = User.class,
                overrides = @OverrideField(field = "email", value = "[a-z]+@test.com", isRegex = true)
        )
})
void testUser(User user) {
    assertTrue(user.getEmail().endsWith("@test.com"));
}
```

## Why Matriarch?

Traditional test data creation can be tedious and repetitive. Matriarch eliminates boilerplate by:

1. **Automatic Generation** - Generates realistic test data automatically
2. **Flexible Overrides** - Override only the fields you care about for each test
3. **Pattern Matching** - Apply consistent patterns across your test suite
4. **Type Safety** - Fully type-safe API with compile-time checks
5. **Easy Integration** - Works seamlessly with existing JUnit tests

## Getting Help

- Check the [API Reference]({{< ref "api-reference.md" >}}) for detailed method documentation
- See [Examples]({{< ref "examples.md" >}}) for common use cases
- Visit our [GitHub repository](https://github.com/giro-dev/matriarch) for source code and issues

Ready to get started? Head over to the [Getting Started]({{< ref "getting-started.md" >}}) guide!
