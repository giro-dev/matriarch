---
title: "JUnit Integration"
date: 2025-11-15
draft: false
weight: 5
---

# JUnit Integration

Matriarch provides seamless integration with JUnit 5, offering two powerful approaches for test data generation.

## Overview

Matriarch supports two JUnit integration strategies:

1. **@MotherFactoryResource** - For parameterized tests with declarative test cases
2. **@MotherConfig & @Mother** - For field and parameter injection

## Parameterized Tests

### Basic Usage

Use `@MotherFactoryResource` to generate test parameters:

```java
import org.junit.jupiter.params.ParameterizedTest;
import dev.agiro.matriarch.junit.MotherFactoryResource;
import dev.agiro.matriarch.junit.RandomArg;

@ParameterizedTest
@MotherFactoryResource(args = {
        @RandomArg(targetClass = User.class)
})
void testWithRandomUser(User user) {
    assertNotNull(user);
    assertNotNull(user.getEmail());
}
```

### Named Test Cases

Add descriptive names for better test reporting:

```java

@ParameterizedTest(name = "[{index}] {0}")
@MotherFactoryResource(args = {
        @RandomArg(
                name = "Valid User",
                targetClass = User.class
        ),
        @RandomArg(
                name = "Admin User",
                targetClass = User.class,
                overrides = @OverrideField(field = "role", value = "ADMIN")
        ),
        @RandomArg(
                name = "Guest User",
                targetClass = User.class,
                overrides = @OverrideField(field = "role", value = "GUEST")
        )
})
void testUsers(User user) {
    // Test runs 3 times with descriptive names in reports
    assertNotNull(user);
}
```

**Test output:**

```
✓ [1] Valid User
✓ [2] Admin User
✓ [3] Guest User
```

### Field Overrides

Override specific fields using `@OverrideField`:

```java

@ParameterizedTest
@MotherFactoryResource(args = {
        @RandomArg(
                name = "Standard User",
                targetClass = User.class,
                overrides = {
                        @OverrideField(field = "email", value = "standard@example.com"),
                        @OverrideField(field = "role", value = "STANDARD"),
                        @OverrideField(field = "active", value = "true")
                }
        )
})
void testStandardUser(User user) {
    assertEquals("standard@example.com", user.getEmail());
    assertEquals("STANDARD", user.getRole());
    assertTrue(user.isActive());
}
```

### Regex Pattern Support

Generate random data matching patterns using `isRegex = true`:

```java

@ParameterizedTest
@MotherFactoryResource(args = {
        @RandomArg(
                name = "Email Pattern Test",
                targetClass = User.class,
                overrides = {
                        @OverrideField(
                                field = "email",
                                value = "[a-z]{5,10}@(gmail|outlook)\\.com",
                                isRegex = true
                        ),
                        @OverrideField(
                                field = "phoneNumber",
                                value = "\\d{3}-\\d{3}-\\d{4}",
                                isRegex = true
                        ),
                        @OverrideField(
                                field = "userId",
                                value = "USER-\\d{6}",
                                isRegex = true
                        )
                }
        )
})
void testWithPatterns(User user) {
    assertTrue(user.getEmail().matches(".*@(gmail|outlook)\\.com"));
    assertTrue(user.getPhoneNumber().matches("\\d{3}-\\d{3}-\\d{4}"));
    assertTrue(user.getUserId().startsWith("USER-"));
}
```

### Nested Field Overrides

Use dot notation for nested objects:

```java

@ParameterizedTest
@MotherFactoryResource(args = {
        @RandomArg(
                targetClass = Order.class,
                overrides = {
                        @OverrideField(field = "customer.name", value = "John Doe"),
                        @OverrideField(field = "customer.email", value = "john@example.com"),
                        @OverrideField(field = "shippingAddress.city", value = "New York"),
                        @OverrideField(field = "shippingAddress.zipCode", value = "10001")
                }
        )
})
void testOrder(Order order) {
    assertEquals("John Doe", order.getCustomer().getName());
    assertEquals("New York", order.getShippingAddress().getCity());
}
```

### JSON Overrides

For complex nested structures, use JSON format:

```java
@ParameterizedTest
@MotherFactoryResource(args = {
    @RandomArg(
        targetClass = CreatePurchaseCommand.class,
        jsonOverrides = """
            {
                "version": 3,
                "status": "PENDING",
                "partRequestList": [
                    {
                        "part": "PART-001",
                        "quantity": 5
                    },
                    {
                        "part": "PART-002",
                        "quantity": 10
                    }
                ],
                "user": {
                    "email": "test@example.com",
                    "role": "ADMIN",
                    "preferences": {
                        "notifications": true,
                        "theme": "dark"
                    }
                }
            }
            """,
        overrides = {
            @OverrideField(
                field = "actionId",
                value = "ACT-\\d{6}",
                isRegex = true
            )
        }
    )
})
void testComplexCommand(CreatePurchaseCommand command) {
    assertEquals(3, command.getVersion());
    assertEquals(2, command.getPartRequestList().size());
    assertEquals("test@example.com", command.getUser().getEmail());
    assertTrue(command.getActionId().startsWith("ACT-"));
}
```

**JSON + Array Overrides:**

- By default, JSON overrides take precedence
- Array `overrides` can still add or override individual fields
- Both are merged into the final object

### Override Types

Specify the override type explicitly:

```java

@ParameterizedTest
@MotherFactoryResource(args = {
        @RandomArg(
                targetClass = User.class,
                overrides = {
                        // String value (default)
                        @OverrideField(
                                field = "name",
                                value = "John Doe",
                                type = OverrideType.STRING
                        ),

                        // Regex pattern
                        @OverrideField(
                                field = "email",
                                value = "[a-z]+@test.com",
                                type = OverrideType.REGEX
                        ),

                        // Null value
                        @OverrideField(
                                field = "middleName",
                                type = OverrideType.NULL
                        )
                }
        )
})
void testOverrideTypes(User user) {
    assertEquals("John Doe", user.getName());
    assertNull(user.getMiddleName());
    assertTrue(user.getEmail().endsWith("@test.com"));
}
```

**Available Types:**

- `STRING` (default) - Direct string value
- `REGEX` - Generate from regex pattern
- `NULL` - Set field to null
- `OBJECT` - Not commonly used in annotations

### Multiple Test Cases

Generate multiple test cases in one annotation:

```java

@ParameterizedTest(name = "Testing {0}")
@MotherFactoryResource(args = {
        @RandomArg(name = "Admin with full access", targetClass = User.class,
                overrides = @OverrideField(field = "role", value = "ADMIN")),
        @RandomArg(name = "Standard user with limited access", targetClass = User.class,
                overrides = @OverrideField(field = "role", value = "STANDARD")),
        @RandomArg(name = "Guest with no privileges", targetClass = User.class,
                overrides = @OverrideField(field = "role", value = "GUEST")),
        @RandomArg(name = "Inactive user", targetClass = User.class,
                overrides = {
                        @OverrideField(field = "role", value = "STANDARD"),
                        @OverrideField(field = "active", value = "false")
                })
})
void testUsersByRole(User user) {
    // Runs 4 times with different configurations
    assertNotNull(user.getRole());
}
```

## Field and Parameter Injection

### @MotherConfig Annotation

Enable automatic field and parameter injection:

```java
import dev.agiro.matriarch.junit.MotherConfig;
import dev.agiro.matriarch.junit.Mother;

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
    void testUserUpdate() {
        // Fresh instance for each test
        String oldEmail = testUser.getEmail();
        testUser.setEmail("new@example.com");
        assertNotEquals(oldEmail, testUser.getEmail());
    }
}
```

**Key Points:**

- No need for `@ExtendWith` - `@MotherConfig` enables the extension automatically
- Fields are populated **before each test method**
- Each test gets a fresh instance

### Field Injection with Overrides

Customize injected fields:

```java

@MotherConfig
class OrderServiceTest {

    @Mother(overrides = {
            @OverrideField(field = "status", value = "PENDING"),
            @OverrideField(field = "customer.email", value = "test@example.com")
    })
    Order pendingOrder;

    @Mother(overrides = {
            @OverrideField(field = "status", value = "COMPLETED"),
            @OverrideField(field = "paymentStatus", value = "PAID")
    })
    Order completedOrder;

    @Test
    void testOrders() {
        assertEquals("PENDING", pendingOrder.getStatus());
        assertEquals("COMPLETED", completedOrder.getStatus());
    }
}
```

### Parameter Injection

Inject test objects as method parameters:

```java

@MotherConfig
class PaymentProcessorTest {

    @Test
    void testPayment(@Mother Order order) {
        // Order automatically injected
        assertNotNull(order);
        paymentProcessor.process(order);
    }

    @Test
    void testRefund(@Mother Order order, @Mother User user) {
        // Multiple parameters supported
        assertNotNull(order);
        assertNotNull(user);
        refundService.process(order, user);
    }

    @ParameterizedTest
    @ValueSource(strings = {"CREDIT_CARD", "PAYPAL", "BANK_TRANSFER"})
    void testPaymentMethods(String method, @Mother Order order) {
        // Mix with other parameter sources
        order.setPaymentMethod(method);
        assertTrue(paymentProcessor.supports(order));
    }
}
```

### Parameter Injection with Overrides

Customize parameter injection inline:

```java

@MotherConfig
class AccountTest {

    @Test
    void testAdminAccount(
            @Mother(overrides = {
                    @OverrideField(field = "role", value = "ADMIN"),
                    @OverrideField(field = "permissions", value = "ALL")
            }) User admin
    ) {
        assertEquals("ADMIN", admin.getRole());
        assertTrue(admin.hasAllPermissions());
    }
}
```

### Configuration Options

Configure defaults for all generated objects:

```java

@MotherConfig(
        collectionMin = 5,          // Minimum collection size
        collectionMax = 15,         // Maximum collection size
        jsonOverridesWin = true     // JSON overrides take precedence
)
class MyTest {

    @Mother
    Order order;  // All collections will have 5-15 elements

    @Test
    void testOrder() {
        assertTrue(order.getItems().size() >= 5);
        assertTrue(order.getItems().size() <= 15);
    }
}
```

**Configuration Properties:**

- `collectionMin` - Minimum size for generated Lists/Sets (default: 1)
- `collectionMax` - Maximum size for generated Lists/Sets (default: 15)
- `jsonOverridesWin` - Whether JSON overrides take precedence over array overrides (default: true)
- `seed` - Reserved for future deterministic generation
- `knownPatterns` - Not yet implemented (use patterns.yaml instead)

### JSON Overrides in Field Injection

Use complex JSON structures:

```java

@MotherConfig
class ComplexTest {

    @Mother(jsonOverrides = """
            {
                "customer": {
                    "name": "Jane Doe",
                    "email": "jane@example.com",
                    "address": {
                        "street": "123 Main St",
                        "city": "Boston"
                    }
                },
                "items": [
                    {"name": "Item 1", "price": 19.99},
                    {"name": "Item 2", "price": 29.99}
                ]
            }
            """)
    Order complexOrder;

    @Test
    void testComplexOrder() {
        assertEquals("Jane Doe", complexOrder.getCustomer().getName());
        assertEquals(2, complexOrder.getItems().size());
        assertEquals("Boston", complexOrder.getCustomer().getAddress().getCity());
    }
}
```

## Annotation Reference

### @MotherFactoryResource

Main annotation for parameterized tests:

```java
@MotherFactoryResource(args = { /* RandomArg array */})
```

**Properties:**

- `args` - Array of `@RandomArg` annotations defining test cases

### @RandomArg

Defines a single test case:

```java
@RandomArg(
        name = "Test Case Name",           // Optional: Display name
        targetClass = User.class,          // Required: Class to generate
        overrides = { /* OverrideField[] */},  // Optional: Field overrides
        jsonOverrides = "{ ... }"          // Optional: JSON structure
)
```

**Properties:**

- `name` - Descriptive name for test reports (appears in output)
- `targetClass` - The class type to generate
- `overrides` - Array of `@OverrideField` for field-level overrides
- `jsonOverrides` - JSON string for complex nested structures

### @OverrideField

Defines a field override:

```java
@OverrideField(
        field = "fieldName",               // Required: Field path
        value = "value",                   // Value or pattern
        type = OverrideType.STRING,        // Optional: Override type
        isRegex = false                    // Optional: Treat as regex
)
```

**Properties:**

- `field` - Field name (supports dot notation: "address.city")
- `value` - Value to set or regex pattern
- `type` - Override type (STRING, REGEX, NULL, OBJECT)
- `isRegex` - If true, treats value as regex pattern (shorthand for type=REGEX)

### @MotherConfig

Enables field and parameter injection:

```java
@MotherConfig(
        collectionMin = 1,
        collectionMax = 15,
        jsonOverridesWin = true
)
```

**Properties:**

- `collectionMin` - Minimum collection size (default: 1)
- `collectionMax` - Maximum collection size (default: 15)
- `jsonOverridesWin` - JSON precedence (default: true)

### @Mother

Marks fields and parameters for injection:

```java

@Mother(
        overrides = { /* OverrideField[] */},
        jsonOverrides = "{ ... }"
)
User user;
```

**Properties:**

- `overrides` - Field overrides (same as RandomArg)
- `jsonOverrides` - JSON structure (same as RandomArg)

## Best Practices

### 1. Use Named Test Cases

```java
// Good - Clear test reporting
@ParameterizedTest(name = "[{index}] {0}")
@MotherFactoryResource(args = {
        @RandomArg(name = "Admin User",...),
@RandomArg(name = "Standard User", ...)
        })

// Bad - Generic output
@ParameterizedTest
@MotherFactoryResource(args = {
        @RandomArg(targetClass = User.class),
        @RandomArg(targetClass = User.class)
})
```

### 2. Prefer isRegex for Patterns

```java
// Good - Concise
@OverrideField(field = "email", value = "[a-z]+@test.com", isRegex = true)

// Verbose
@OverrideField(field = "email", value = "[a-z]+@test.com", type = OverrideType.REGEX)
```

### 3. Use JSON for Complex Structures

```java
// Good - Readable structure
@RandomArg(
        targetClass = Order.class,
        jsonOverrides = """
                {
                    "items": [...],
                    "customer": {...}
                }
                """
)

// Bad - Too many individual overrides
@RandomArg(
        targetClass = Order.class,
        overrides = {
                @OverrideField(field = "items[0].name",...),
@OverrideField(field = "items[0].price", ...),
        // ... many more
        }
        )
```

### 4. Combine Injection Strategies

```java

@MotherConfig
class ServiceTest {

    @Mother  // For common test data
    User defaultUser;

    @Test
    void testWithDefault() {
        service.process(defaultUser);
    }

    @Test
    void testWithCustom(@Mother(overrides = {
            @OverrideField(field = "role", value = "ADMIN")
    }) User adminUser) {
        // Custom data when needed
        service.processAdmin(adminUser);
    }
}
```

### 5. Group Related Test Cases

```java

@ParameterizedTest(name = "Role: {0}")
@MotherFactoryResource(args = {
        @RandomArg(name = "ADMIN", targetClass = User.class,
                overrides = @OverrideField(field = "role", value = "ADMIN")),
        @RandomArg(name = "MANAGER", targetClass = User.class,
                overrides = @OverrideField(field = "role", value = "MANAGER")),
        @RandomArg(name = "USER", targetClass = User.class,
                overrides = @OverrideField(field = "role", value = "USER"))
})
void testAllRoles(User user) {
    // Single test method for all role variations
}
```

## Complete Examples

### Parameterized Test Suite

```java
class UserValidationTest {

    @ParameterizedTest(name = "[{index}] {0}")
    @MotherFactoryResource(args = {
            @RandomArg(
                    name = "Valid email format",
                    targetClass = User.class,
                    overrides = @OverrideField(
                            field = "email",
                            value = "[a-z]{5,10}@(gmail|yahoo)\\.com",
                            isRegex = true
                    )
            ),
            @RandomArg(
                    name = "Valid phone number",
                    targetClass = User.class,
                    overrides = @OverrideField(
                            field = "phoneNumber",
                            value = "\\d{3}-\\d{3}-\\d{4}",
                            isRegex = true
                    )
            ),
            @RandomArg(
                    name = "Adult user",
                    targetClass = User.class,
                    overrides = @OverrideField(field = "age", value = "25")
            )
    })
    void testValidUsers(User user) {
        assertTrue(validator.isValid(user));
    }
}
```

### Field Injection Test

```java

@MotherConfig(collectionMin = 3, collectionMax = 5)
class OrderProcessingTest {

    @Mother(overrides = {
            @OverrideField(field = "status", value = "PENDING"),
            @OverrideField(field = "paymentStatus", value = "UNPAID")
    })
    Order pendingOrder;

    @Autowired
    OrderService orderService;

    @Test
    void testProcessPendingOrder() {
        orderService.process(pendingOrder);
        assertEquals("COMPLETED", pendingOrder.getStatus());
    }

    @Test
    void testCancelOrder() {
        orderService.cancel(pendingOrder);
        assertEquals("CANCELLED", pendingOrder.getStatus());
    }

    @Test
    void testWithCustomOrder(@Mother(overrides = {
            @OverrideField(field = "priority", value = "HIGH"),
            @OverrideField(field = "expressShipping", value = "true")
    }) Order expressOrder) {
        assertTrue(orderService.requiresImmediateProcessing(expressOrder));
    }
}
```

## Next Steps

- **[Pattern System]({{< ref "pattern-system.md" >}})** - Configure reusable patterns
- **[Advanced Usage]({{< ref "advanced-usage.md" >}})** - Complex scenarios
- **[Examples]({{< ref "examples.md" >}})** - More real-world examples
- **[API Reference]({{< ref "api-reference.md" >}})** - Complete API documentation

