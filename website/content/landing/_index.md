---
title: Matriarch
linkTitle: landing
---

# About Matriarch

**A powerful ObjectMother library for flexible test data generation in Java**

Matriarch is a modern, lightweight Java library designed to simplify test data generation. Born from the need to eliminate repetitive test setup code, Matriarch provides an elegant solution for creating fully-populated test objects with minimal boilerplate.

---

## Why Matriarch?

Traditional test data creation is tedious and repetitive. Matriarch eliminates the boilerplate.

### Before Matriarch

```java
@Test
void testUserCreation() {
    User user = new User();
    user.setName("John Doe");
    user.setEmail("john@example.com");
    user.setAge(30);
    user.setRole("STANDARD");
    user.setCreatedAt(Instant.now());
    user.setActive(true);
    user.setAddress(new Address());
    user.getAddress().setStreet("123 Main St");
    user.getAddress().setCity("New York");
    user.getAddress().setZipCode("10001");
    // ... many more fields
    
    // Finally, test something
    assertNotNull(user);
}
```

### With Matriarch

```java
@Test
void testUserCreation() {
    User user = Mother.forClass(User.class)
        .forField("email", "john@example.com")
        .build();
    
    // All fields populated automatically
    assertNotNull(user);
}
```

---

## Key Features

### ✨ Smart & Flexible
Generate realistic test data automatically or override specific fields. Matriarch adapts to your needs with intelligent defaults and precise control.

### 🎯 Flexible Object Generation
Create fully populated test objects with random or specified values. Override only the fields that matter for your test.

### 📝 Builder Pattern
Fluent, readable API for test data creation. Chain methods naturally for complex scenarios.

### 🔤 Regex Pattern Support
Generate data matching regex patterns with built-in generator. Perfect for validating formats.

### 🧪 JUnit Integration
Seamless integration with JUnit ParameterizedTest. Field and parameter injection out of the box.

### 🧩 43+ Built-in Suppliers
Ready-to-use suppliers for names, emails, addresses, companies, and more. No external data libraries needed.

### 🎲 Multiple Strategies
Use as builder, factory, or JUnit annotation. Choose what fits your testing style.

### ⚡ Zero Dependencies
Lightweight and fast with no heavy external libraries. Just add to your test scope and start generating.

---

## Use Cases

**🧪 Unit Testing**  
Generate test objects quickly for service and controller tests without manual setup.

**🔄 Integration Testing**  
Create realistic test data for database operations and API endpoint validation.

**📊 Parameterized Tests**  
Run the same test with multiple data variations using JUnit annotations.

**🎲 Fuzz Testing**  
Generate random variations to test edge cases and error handling.

**💾 Data Seeding**  
Populate test databases with realistic data for development and testing.

**📈 Performance Testing**  
Generate large datasets efficiently with streams for load testing scenarios.

---

## Technology Stack

**Built with modern Java practices**

- ☕ **Java 17+** - Modern language features
- 🧪 **JUnit 5** - Seamless integration
- 📦 **Jackson** - JSON processing
- 🎨 **No heavy dependencies** - Lightweight and fast

**Lightweight & Fast**

Matriarch is designed to be minimal yet powerful. No reflection caching overhead, no external data generators, just clean code that gets out of your way.

---

## Project Philosophy

Matriarch follows these core principles:

### 🎯 Simplicity First
Test data generation should be simple and intuitive. If you need to read documentation for basic usage, we've failed.

### 🔒 Type Safety
Compile-time type checking ensures your test data matches your domain model. Catch errors early.

### 🧩 Flexibility Without Complexity
Provide powerful features without overwhelming users. Start simple, scale up when needed.

### 🚫 Zero Magic
No hidden behavior, no surprising defaults. What you write is what you get.

### 😊 Developer Experience
Fluent APIs, clear error messages, and comprehensive documentation. Testing should be enjoyable.

### 🤝 Community Driven
Open source, open to contributions. Built by developers, for developers.

---

## Statistics at a Glance

| Metric | Value |
|--------|-------|
| **Built-in Suppliers** | 43+ |
| **Heavy Dependencies** | Zero |
| **Type Safety** | 100% |
| **Possibilities** | Infinite ∞ |

---

## Getting Started

Ready to simplify your test data generation?

- 📚 **[Get Started](/docs/getting-started/)** - Installation and first steps
- 📖 **[Documentation](/docs/)** - Comprehensive guides
- 💻 **[View on GitHub](https://github.com/giro-dev/matriarch)** - Source code and issues
- 🔬 **[Examples](/docs/examples/)** - Real-world usage patterns

---

## Community & Support

### 💻 GitHub
Source code, issues, and discussions  
[Visit Repository](https://github.com/giro-dev/matriarch)

### 📚 Documentation
Comprehensive guides and API reference  
[Read Documentation](/docs/)

### 🔬 Examples
Real-world usage and patterns  
[See Examples](/docs/examples/)

### 🧩 Suppliers Reference
43+ built-in data generators  
[Browse Suppliers](/docs/suppliers/)

---

## Contributing

Matriarch is open source software. Contributions are welcome!

Visit our [GitHub repository](https://github.com/giro-dev/matriarch) to:
- 🐛 Report issues
- 💡 Suggest features
- 🔧 Submit pull requests
- 💬 Join discussions

---

## License

Matriarch is open source software.

**Made with ❤️ by developers who love testing**

---

## Author

**Albert Giró Quer**

A passionate developer focused on improving the testing experience for Java developers.



