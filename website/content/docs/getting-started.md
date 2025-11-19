---
title: "Getting Started"
date: 2025-11-15
draft: false
weight: 2
---

# Getting Started with Matriarch

This guide will help you get up and running with Matriarch in minutes.

## Installation

Add the following dependency to your build system:
{{< tabs items="Maven,Gradle" >}}

{{< tab >}}
In the `pom.xml` file, add:

```xml

<dependency>
    <groupId>dev.agiro</groupId>
    <artifactId>matriarch</artifactId>
    <version>1.1.0</version>
    <scope>test</scope>
</dependency>
```

{{< /tab >}}
{{< tab >}}
In the `build.gradle` or `build.gradle.kts` file, add:

```kotlin
testImplementation("dev.agiro:matriarch:1.1.0")
```

{{< /tab >}}

{{< /tabs >}}

## Your First Test Object

Let's take a simple User class to work with:

```java
import dev.agiro.matriarch.Mother;

// Define a simple class
public class User {
    private String name;
    private String email;
    private String role;
    private int age;

    // Getters and setters...
}
```

create a simple test object using Matriarch:

```java
User randomUser = Mother.forClass(User.class).build();
```

That's it! Matriarch will automatically populate all fields with random, valid data.

## Define Specific Fields

Often you need to control specific field values:

```java
User admin = Mother.forClass(User.class)
        .forField("email", "admin@example.com")
        .forField("role", "ADMIN")
        .forField("age", 30)
        .build();
```

## Generate Collections

Need multiple test objects? Generate collections easily:

```java
List<User> users = Mother.forClass(User.class).buildList(10);
Set<User> uniqueUsers = Mother.forClass(User.class).buildSet(5);
Stream<User> userStream = Mother.forClass(User.class).buildStream(100);
```

## Use with JUnit

Integrate with JUnit ParameterizedTest for powerful test scenarios:

```java
import dev.agiro.matriarch.junit.MotherConfig;
import dev.agiro.matriarch.junit.Mother;
import dev.agiro.matriarch.junit.annotations.Mother;

@MotherConfig  // This automatically enables the JUnit extension
class UserServiceTest {

    @Mother
    User testUser;  // Populated before each test

    @Test
    void testUserCreation() {
        assertNotNull(testUser);
        assertNotNull(testUser.getEmail());
    }

    @Test
    void testUserUpdate(@Mother User otherUser) {
        // Fresh instance for each test
        String oldEmail = otherUser.getEmail();
        otherUser.setEmail("new@example.com");
        assertNotEquals(oldEmail, otherUser.getEmail());
    }
}
```

## Nested Objects

Matriarch handles nested objects and complex structures automatically:

```java
public class Order {
    private String orderId;
    private User customer;  // Nested object
    private Address shippingAddress;  // Another nested object
    private List<Item> items;  // Collection of nested objects
}

// All nested objects are populated automatically
Order order = Mother.forClass(Order.class).build();

// Override nested fields using dot notation
Order order = Mother.forClass(Order.class)
        .forField("customer.email", "customer@example.com")
        .forField("shippingAddress.city", "New York")
        .build();
```

## Regex Patterns

Generate data matching specific patterns:

```java
User user = Mother.forClass(User.class)
        .forField("email", new Regex("[a-z]{5,10}@gmail.com"))
        .forField("phoneNumber", new Regex("\\d{3}-\\d{3}-\\d{4}"))
        .forField("zipCode", new Regex("\\d{5}"))
        .build();
```

## Next Steps

Now that you've seen the basics, explore more advanced features:

- **[Core Concepts]({{< ref "core-concepts.md" >}})** - Understand the underlying principles
- **[Builder API]({{< ref "builder-api.md" >}})** - Learn all builder methods and options
- **[JUnit Integration]({{< ref "junit-integration.md" >}})** - Deep dive into test integration
- **[Pattern System]({{< ref "pattern-system.md" >}})** - Configure patterns for your domain
- **[Examples]({{< ref "examples.md" >}})** - See real-world use cases

## Tips for Success

1. **Start Simple** - Begin with basic object generation and add complexity as needed
2. **Override Selectively** - Only override fields that matter for your test
3. **Use Named Tests** - Add names to parameterized tests for better reporting
4. **Leverage Patterns** - Configure patterns for consistent data across tests
5. **Combine Strategies** - Mix builder API with JUnit annotations as appropriate

Ready to dive deeper? Continue to [Core Concepts]({{< ref "core-concepts.md" >}}) to understand how Matriarch works
under the hood.

