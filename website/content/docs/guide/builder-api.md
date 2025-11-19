---
title: "Builder API"
date: 2025-11-15
draft: false
weight: 4
---

# Builder API Reference

The Builder API is the most flexible way to use Matriarch. This guide covers all available methods and their use cases.

## Basic Usage

### Creating a Builder

```java
Mother.forClass(User.class)
```

Every Matriarch operation starts with `Mother.forClass(Class<T>)`, which returns a builder for that type.

### Building Objects

```java
// Single object
User user = Mother.forClass(User.class).build();

// List of objects
List<User> users = Mother.forClass(User.class).buildList(10);

// Set of objects
Set<User> uniqueUsers = Mother.forClass(User.class).buildSet(5);

// Stream of objects (lazy generation)
Stream<User> userStream = Mother.forClass(User.class).buildStream(100);
```

## Field Overrides

### forField() - The Primary Method

The `.forField()` method is the recommended way to override field values:

```java
Mother.forClass(User.class)
    .forField("name", "John Doe")
    .forField("email", "john@example.com")
    .forField("age", 30)
    .build();
```

**Signature:**
```java
.forField(String fieldPath, Object value)
.forField(String fieldPath, String pattern, Overrider.OverriderType type)
```

### override() - Legacy Alias

For backward compatibility, `.override()` remains available as an alias:

```java
Mother.forClass(User.class)
    .override("name", "John Doe")  // Same as .forField()
    .build();
```

> 💡 **Recommendation:** Use `.forField()` for clarity in new code.

### Nested Field Access

Use dot notation to access nested objects:

```java
Mother.forClass(Order.class)
    .forField("customer.name", "Jane Doe")
    .forField("customer.email", "jane@example.com")
    .forField("shippingAddress.street", "123 Main St")
    .forField("shippingAddress.city", "New York")
    .forField("shippingAddress.zipCode", "10001")
    .build();
```

### Collection Access

#### List Elements

Access elements by index using `[index]`:

```java
Mother.forClass(Order.class)
    .forField("items[0].name", "First Item")
    .forField("items[0].price", 19.99)
    .forField("items[1].name", "Second Item")
    .forField("items[2].quantity", 5)
    .build();
```

**Notes:**
- Default list size is 1-15 elements (random)
- If you specify an index beyond the random size, the list will grow to accommodate it
- Unspecified indices get random values

#### Map Values

Access map values by key using `[key]`:

```java
Mother.forClass(Configuration.class)
    .forField("settings[timeout].value", "30")
    .forField("settings[retries].value", "3")
    .forField("settings[maxConnections].value", "100")
    .build();
```

**Limitations:**
- Maps are limited to `Map<Object, Object>`
- Key must be convertible to the map's key type
- Automatic random map generation is not supported (use `Overrider.object()` for full maps)

## Override Types

### String Values

Direct string values are automatically converted to the target field type:

```java
Mother.forClass(User.class)
    .forField("name", "John Doe")           // String field
    .forField("age", "30")                  // Converted to int
    .forField("active", "true")             // Converted to boolean
    .forField("role", "ADMIN")              // Converted to enum
    .forField("createdAt", "2024-01-15")    // Converted to LocalDate
    .build();
```

### Regex Patterns

Generate values matching regex patterns:

```java
// Method 1: Using Overrider.regex()
Mother.forClass(User.class)
    .forField("email", Overrider.regex("[a-z]{5,10}@(gmail|outlook)\\.com"))
    .forField("phoneNumber", Overrider.regex("\\d{3}-\\d{3}-\\d{4}"))
    .build();

// Method 2: Using type parameter
Mother.forClass(User.class)
    .forField("zipCode", "\\d{5}", Overrider.OverriderType.REGEX)
    .forField("userId", "USER-\\d{6}", Overrider.OverriderType.REGEX)
    .build();

// Method 3: Using Regex wrapper (in annotations)
import dev.agiro.matriarch.Regex;

Mother.forClass(User.class)
    .forField("email", new Regex("[a-z]+@test.com"))
    .build();
```

**Supported Regex Features:**
- Character classes: `[a-z]`, `[A-Z]`, `[0-9]`, `[abc]`
- Quantifiers: `{n}`, `{n,m}`, `*`, `+`, `?`
- Groups with alternation: `(option1|option2|option3)`
- Escaped characters: `\d` (digit), `\w` (word char), `\s` (space)
- Complex nested patterns

### Object Values

Pass objects directly:

```java
Mother.forClass(Order.class)
    .forField("customer", new Customer("John", "john@example.com"))
    .forField("createdAt", Instant.now())
    .forField("shippingAddress", new Address("123 Main St", "New York"))
    .build();

// Using Overrider.object() for clarity
Mother.forClass(Order.class)
    .forField("metadata", Overrider.object(Map.of("key", "value")))
    .build();
```

### Null Values

Explicitly set fields to null:

```java
// Method 1: Direct null
Mother.forClass(User.class)
    .forField("middleName", null)
    .forField("nickname", null)
    .build();

// Method 2: Using Overrider.nullValue()
Mother.forClass(User.class)
    .forField("optionalField", Overrider.nullValue())
    .forField("metadata", Overrider.nullValue())
    .build();
```

## Custom Generators

### Field-Specific Generators

Use `.forField()` with a `Supplier` for dynamic generation:

```java
import java.util.function.Supplier;

Mother.forClass(User.class)
    .forField("email", (Supplier<String>) () -> 
        faker.internet().emailAddress())
    .forField("birthDate", (Supplier<LocalDate>) () -> 
        LocalDate.now().minusYears(random.nextInt(18, 65)))
    .forField("accountId", (Supplier<String>) () -> 
        "ACC-" + UUID.randomUUID().toString())
    .build();
```

**Use cases:**
- Integration with external libraries (Faker, RandomUtils)
- Dynamic values based on runtime conditions
- Complex generation logic
- Values that depend on external state

### Type-Based Generators

Use `.forType()` to provide generators for **all fields** of a specific type:

```java
Mother.forClass(Order.class)
    // All LocalDate fields use this generator
    .forType(LocalDate.class, () -> 
        LocalDate.now().minusDays(random.nextInt(365)))
    
    // All BigDecimal fields use this generator
    .forType(BigDecimal.class, () -> 
        BigDecimal.valueOf(random.nextDouble() * 1000)
                  .setScale(2, RoundingMode.HALF_UP))
    
    // All UUID fields use this generator
    .forType(UUID.class, UUID::randomUUID)
    
    .build();
```

**Use cases:**
- Consistent date ranges across the object graph
- Domain-specific number formats
- Custom string formats for specific types
- Preventing nulls for certain types

### Combining Generators

Field-specific generators override type-based generators:

```java
Mother.forClass(Invoice.class)
    // All LocalDate fields default to past dates
    .forType(LocalDate.class, () -> 
        LocalDate.now().minusDays(30))
    
    // Except these specific fields
    .forField("dueDate", (Supplier<LocalDate>) () -> 
        LocalDate.now().plusDays(30))
    .forField("paymentDate", (Supplier<LocalDate>) () -> 
        LocalDate.now().plusDays(60))
    
    .build();
```

**Precedence Order:**
1. Explicit field value: `.forField("field", value)`
2. Field-specific generator: `.forField("field", supplier)`
3. Type-based generator: `.forType(Class, supplier)`
4. Pattern matching
5. Random generation

## Collection Control

### withCollectionSize()

Control the size of all generated collections:

```java
Mother.forClass(Order.class)
    .withCollectionSize(5, 10)  // All Lists/Sets: 5-10 elements
    .build();
```

**Parameters:**
- `min` - Minimum collection size (inclusive)
- `max` - Maximum collection size (inclusive)

**Examples:**

```java
// Fixed size collections
Mother.forClass(Order.class)
    .withCollectionSize(5, 5)  // Exactly 5 elements
    .build();

// Allow empty collections
Mother.forClass(User.class)
    .withCollectionSize(0, 3)  // 0-3 elements
    .build();

// Large collections
Mother.forClass(Report.class)
    .withCollectionSize(50, 100)  // 50-100 elements
    .build();

// Default behavior (no call to withCollectionSize)
Mother.forClass(Order.class).build();  // 1-15 elements (random)
```

### Collection Types

Matriarch supports various collection types:

```java
// Lists
List<String> items;          // Generated with random size (1-15)
ArrayList<User> users;       // Concrete implementation
LinkedList<Order> orders;    // Alternative implementation

// Sets
Set<String> tags;            // Generated with random size (1-15)
HashSet<Integer> ids;        // Concrete implementation
TreeSet<Long> timestamps;    // Sorted set

// Custom collection sizes
Mother.forClass(Container.class)
    .withCollectionSize(10, 20)
    .build();  // All collections: 10-20 elements
```

## Excluding Fields

### excludeFields()

Skip generation for specific fields (they remain null):

```java
Mother.forClass(User.class)
    .excludeFields("password", "securityToken", "internalId")
    .build();
```

**Parameters:**
- Varargs of field names to exclude

**Use cases:**
- Sensitive data that shouldn't be populated
- Fields that cause circular dependencies
- Optional fields not needed for tests
- Fields with complex initialization requirements

**Example with nested fields:**

```java
Mother.forClass(Account.class)
    .excludeFields(
        "encryptedPassword",
        "user.securityToken",
        "user.metadata",
        "auditLog"
    )
    .forField("username", "testuser")
    .build();
```

## Method Chaining

All builder methods return the builder instance, allowing fluent chaining:

```java
User user = Mother.forClass(User.class)
    .forField("name", "John Doe")
    .forField("email", "john@example.com")
    .forType(LocalDate.class, () -> LocalDate.now().minusYears(30))
    .withCollectionSize(5, 10)
    .excludeFields("password", "internalId")
    .build();
```

**Order doesn't matter** (except for precedence rules):

```java
// These are equivalent
Mother.forClass(User.class)
    .withCollectionSize(5, 10)
    .forField("name", "John")
    .build();

Mother.forClass(User.class)
    .forField("name", "John")
    .withCollectionSize(5, 10)
    .build();
```

## Complete Example

Here's a comprehensive example using multiple features:

```java
public class OrderBuilderExample {
    
    private Random random = new Random();
    
    @Test
    void buildComplexOrder() {
        Order order = Mother.forClass(Order.class)
            // Explicit values
            .forField("orderId", "ORD-12345")
            .forField("status", "PENDING")
            
            // Nested objects
            .forField("customer.name", "Jane Doe")
            .forField("customer.email", "jane@example.com")
            .forField("customer.loyaltyTier", "GOLD")
            
            // Regex patterns
            .forField("trackingNumber", Overrider.regex("TRK-\\d{10}"))
            .forField("invoiceNumber", "INV-\\d{6}", Overrider.OverriderType.REGEX)
            
            // Collection elements
            .forField("items[0].name", "Premium Widget")
            .forField("items[0].quantity", 2)
            .forField("items[1].name", "Standard Gadget")
            
            // Custom generators
            .forField("orderDate", (Supplier<Instant>) () -> 
                Instant.now().minus(7, ChronoUnit.DAYS))
            .forField("confirmationCode", (Supplier<String>) () -> 
                "CONF-" + UUID.randomUUID().toString().substring(0, 8))
            
            // Type-based generators
            .forType(BigDecimal.class, () -> 
                BigDecimal.valueOf(random.nextDouble() * 1000)
                          .setScale(2, RoundingMode.HALF_UP))
            .forType(LocalDate.class, () -> 
                LocalDate.now().minusDays(random.nextInt(30)))
            
            // Collection control
            .withCollectionSize(2, 5)
            
            // Exclude fields
            .excludeFields("internalNotes", "processingMetadata")
            
            .build();
        
        // Verify
        assertEquals("ORD-12345", order.getOrderId());
        assertEquals("Jane Doe", order.getCustomer().getName());
        assertTrue(order.getTrackingNumber().startsWith("TRK-"));
        assertNull(order.getInternalNotes());
    }
}
```

## Next Steps

- **[JUnit Integration]({{< ref "junit-integration.md" >}})** - Use builders with JUnit annotations
- **[Pattern System]({{< ref "pattern-system.md" >}})** - Configure reusable patterns
- **[Advanced Usage]({{< ref "advanced-usage.md" >}})** - Complex scenarios and best practices
- **[API Reference]({{< ref "api-reference.md" >}})** -Complete method documentation

