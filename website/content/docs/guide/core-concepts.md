---
title: "Core Concepts"
date: 2025-11-15
draft: false
weight: 3
---

Understanding these core concepts will help you use Matriarch effectively.

## The ObjectMother Pattern

Matriarch implements the **ObjectMother** pattern, a test pattern that provides a central place to create test objects. Instead of manually creating objects (`class`, `record`) with all their properties in each test, ObjectMother does it for you.

### Traditional Approach (Without Matriarch)

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
    
    // All fields are populated, only override what matters
    assertNotNull(user);
}
```

## Three Usage Strategies

Matriarch offers three complementary ways to generate test data:

### 1. Builder API (Recommended)

The most flexible and readable approach:

```java
User user = Mother.forClass(User.class)
    .forField("email", "test@example.com")
    .forField("age", 25)
    .build();
```

**Use when:**
- Building objects in test methods
- You need maximum control and flexibility
- Creating one-off test scenarios

### 2. JUnit Annotations

Declarative approach for parameterized tests:

```java
@ParameterizedTest
@MotherFactoryResource(args = {
    @RandomArg(targetClass = User.class)
})
void testUser(User user) {
    assertNotNull(user);
}
```

**Use when:**
- Running the same test with multiple data sets
- You want test cases defined at declaration time
- Creating data-driven tests

### 3. Field/Parameter Injection

Automatic injection into test fields and parameters:

```java
@MotherConfig
class MyTest {
    @Mother
    User user;  // Automatically populated before each test
    
    @Test
    void test(@Mother Order order) {
        // Parameter automatically injected
    }
}
```

**Use when:**
- You need fresh instances for every test
- Reducing boilerplate in test setup
- Working with test fixtures

## Field Override System

Matriarch's field override system supports nested objects using **dot notation**:

```java
Order order = Mother.forClass(Order.class)
    .forField("customer.email", "customer@example.com")
    .forField("shippingAddress.city", "New York")
    .forField("shippingAddress.zipCode", "10001")
    .build();
```

### Collection Indexing

Access specific elements in lists and maps:

```java
// Override specific list element
Mother.forClass(Order.class)
    .forField("items[0].name", "First Item")
    .forField("items[2].price", 29.99)
    .build();

// Override specific map value
Mother.forClass(Config.class)
    .forField("settings[timeout].value", "30")
    .forField("settings[retries].value", "3")
    .build();
```

### Override Types

Matriarch supports multiple override types:

#### 1. String Values

```java
.forField("name", "John Doe")
.forField("status", "ACTIVE")
```

#### 2. Regex Patterns

```java
// Using Overrider.regex()
.forField("email", Overrider.regex("[a-z]{5,10}@gmail.com"))

// Using type parameter
.forField("phoneNumber", "\\d{3}-\\d{3}-\\d{4}", Overrider.OverriderType.REGEX)
```

#### 3. Objects

```java
// Direct object
.forField("address", new Address("123 Main St", "New York"))

// Using Overrider.object()
.forField("createdAt", Overrider.object(Instant.now()))
```

#### 4. Suppliers

```java
// Direct object
.forField("address", new AddressSupplier())

// Using Overrider.object()
.forField("createdAt", () -> Instant.now().minus(1, ChronoUnit.DAYS))
```

#### 5. Null Values

```java
// Explicit null
.forField("middleName", null)

// Using Overrider.nullValue()
.forField("optionalField", Overrider.nullValue())
```

## Generator Hierarchy

Matriarch applies generators in a specific order of precedence:

{{% steps %}}

### 1 highest - Explicit Field Values
```java
    .forField("customer.email", "customer@example.com")
```

### 2 - Custom Field Generators
```java
    .forField("createdAt", () -> Instant.now().minus(7, ChronoUnit.DAYS))
```

### 3 - Type-based Generators
```java
    .forType(LocalDate.class, () -> LocalDate.now().minusDays(30))
```
### 4 - Known Patterns Matching
1. environment Variables / JVM System Properties
2. YAML Configuration
3. Built-in Defaults

### 5 lowest - Random Generation. 
If the field is not excluded, a default random value will be generated.

| Type | Generation Strategy |
|------|-------------------|
| `String` | Random alphanumeric (10-20 chars) |
| `int`, `Integer` | Random number (0-1000) |
| `long`, `Long` | Random long |
| `double`, `Double` | Random double (0.0-1000.0) |
| `boolean`, `Boolean` | Random true/false |
| `LocalDate` | Random date (±365 days from now) |
| `LocalDateTime` | Random datetime (±365 days from now) |
| `Instant` | Random instant (±365 days from now) |
| `UUID` | Random UUID |
| `Enum` | Random enum value |
| `List<T>` | List with 1-15 random elements |
| `Set<T>` | Set with 1-15 random elements |
| Nested Objects | Recursively generated |

{{% /steps %}}


## Type Safety

Matriarch is fully type-safe at runtime. Field overrides are validated:

```java
// Correct
Mother.forClass(User.class)
    .forField("age", 30)  // int field
    .build();

// Runtime validation
Mother.forClass(User.class)
    .forField("age", "not a number")  // Will attempt conversion
    .build();
```

Matriarch attempts intelligent type conversion when possible:
- String → int, long, double
- String → LocalDate, LocalDateTime, Instant
- String → Enum values

## Collection Size Control

Control the size of generated collections:

```java
Mother.forClass(Order.class)
    .withCollectionSize(3, 10)  // All collections: 3-10 elements
    .build();

// Default: 1-15 elements
Mother.forClass(Order.class).build();  // Collections have 1-15 items

// Fixed size
Mother.forClass(Order.class)
    .withCollectionSize(5, 5)  // Exactly 5 elements
    .build();

// Allow empty collections
Mother.forClass(Order.class)
    .withCollectionSize(0, 3)  // 0-3 elements (can be empty)
    .build();
```

## Excluding Fields

Sometimes you want to skip certain fields:

```java
User user = Mother.forClass(User.class)
    .excludeFields("password", "securityToken", "internalId")
    .build();

// These fields will be null
assertNull(user.getPassword());
assertNull(user.getSecurityToken());
assertNull(user.getInternalId());
```

**Common use cases:**
- Sensitive data fields
- Fields that cause generation issues
- Optional fields not needed for the test
- Fields with complex dependencies

## Performance Considerations

Matriarch is designed to be lightweight and fast:

- **No Reflection Caching** - Uses efficient reflection with minimal overhead
- **Lazy Generation** - Only generates what you need
- **Stream Support** - Generate large datasets efficiently with streams
- **Minimal Dependencies** - No heavy external libraries

```java
// Efficient: generates 1000 items lazily
Stream<User> users = Mother.forClass(User.class)
    .buildStream(1000);

// Process without loading all into memory
users.filter(u -> u.getAge() > 18)
     .limit(100)
     .forEach(this::processUser);
```


## Next Steps

Now that you understand the core concepts:

- **[Builder API]({{< ref "builder-api.md" >}})** - Explore all builder methods in detail
- **[JUnit Integration]({{< ref "junit-integration.md" >}})** - Learn about test annotations
- **[Pattern System]({{< ref "pattern-system.md" >}})—**Configure patterns for your domain
- **[Advanced Usage]({{< ref "advanced-usage.md" >}})—**Custom generators and complex scenarios

