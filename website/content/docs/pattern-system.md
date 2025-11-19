---
title: "Pattern System"
date: 2025-11-15
draft: false
weight: 6
---

# Pattern System

Matriarch's pattern system allows you to define reusable value generation rules that are automatically applied when
field coordinates match pattern coordinates.

## Overview

Patterns provide a way to configure default values for fields without explicitly overriding them in every test. When a
field's coordinate matches a pattern, Matriarch automatically applies that pattern's generation strategy.

## Pattern Sources

Patterns are loaded from multiple sources with a clear precedence order:

### Precedence Hierarchy

```
1. JVM System Properties (highest priority)
   ↓
2. Custom YAML Patterns
   ↓
3. Built-in Default Patterns (lowest priority)
```

Higher priority sources override lower ones when the same coordinate is defined.

## JVM System Properties

Define patterns via JVM properties using the prefix `matriarch.pattern.`.

### Format

```bash
-Dmatriarch.pattern.<coordinate>="<type>:<value>"
```

Or default to regex type:

```bash
-Dmatriarch.pattern.<coordinate>="<value>"  # Assumes regex type
```

### Examples

```bash
# Explicit type specification
java -Dmatriarch.pattern.email="regex:[a-z]{5,10}@(gmail|outlook)\\.com" \
     -Dmatriarch.pattern.role="list:ADMIN,USER,GUEST" \
     -Dmatriarch.pattern.status="string:ACTIVE" \
     -jar your-tests.jar

# Default to regex (no type prefix)
java -Dmatriarch.pattern.phoneNumber="\\d{3}-\\d{3}-\\d{4}" \
     -jar your-tests.jar
```

### With Maven

```bash
mvn -Dmatriarch.pattern.email="regex:[a-z]+@test\\.com" \
    -Dmatriarch.pattern.userId="regex:USER-\\d{6}" \
    test
```

### In Maven Surefire Plugin

Map environment variables to JVM properties:

```xml

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0</version>
    <configuration>
        <argLine>
            -Dmatriarch.pattern.email=${env.MATRIARCH_PATTERN_EMAIL}
            -Dmatriarch.pattern.apiKey=${env.MATRIARCH_PATTERN_API_KEY}
            -Dmatriarch.pattern.environment=${env.MATRIARCH_PATTERN_ENVIRONMENT}
        </argLine>
    </configuration>
</plugin>
```

Then set environment variables:

```bash
export MATRIARCH_PATTERN_EMAIL="regex:[a-z]+@company\\.com"
export MATRIARCH_PATTERN_API_KEY="regex:KEY-[A-Z0-9]{16}"
export MATRIARCH_PATTERN_ENVIRONMENT="list:DEV,TEST,STAGING,PROD"
mvn test
```

### In Gradle

```kotlin
tasks.test {
    systemProperty("matriarch.pattern.email", "[a-z]+@test\\.com")
    systemProperty("matriarch.pattern.userId", "USER-\\d{6}")
}
```

## Custom YAML Patterns

Create a `patterns.yaml` file in `src/test/resources/` to define custom patterns.

### File Location

```
src/test/resources/patterns.yaml
```

### YAML Format

```yaml
patterns:
  - coordinate: email
    value: "[a-z]{5,10}@(gmail|outlook).com"
    type: regex

  - coordinate: phoneNumber
    value: "\\d{3}-\\d{3}-\\d{4}"
    type: regex

  - coordinate: user.role
    value: "ADMIN,STANDARD,VISITOR"
    type: list

  - coordinate: status
    value: "ACTIVE"
    type: string

  - coordinate: plantId
    value: "\\d{4}"
    type: regex

  - coordinate: priority
    value: "HIGH,MEDIUM,LOW"
    type: list
```

### Pattern Types

#### 1. String Type

Returns the exact value as-is:

```yaml
- coordinate: environment
  value: "TEST"
  type: string
```

```java
// Any field matching "environment" gets "TEST"
User user = Mother.forClass(User.class).build();
// user.environment == "TEST"
```

#### 2. Regex Type

Generates a random string matching the regex pattern:

```yaml
- coordinate: email
  value: "[a-z]{5,10}@company\\.com"
  type: regex
```

```java
// Any field matching "email" gets a generated email
User user = Mother.forClass(User.class).build();
// user.email might be: "hello@company.com", "test@company.com", etc.
```

#### 3. List Type

Randomly selects one value from a comma-separated list:

```yaml
- coordinate: role
  value: "ADMIN,USER,GUEST,MODERATOR"
  type: list
```

```java
// Any field matching "role" gets a random selection
User user = Mother.forClass(User.class).build();
// user.role might be: "ADMIN", "USER", "GUEST", or "MODERATOR"
```

## Pattern Matching

Patterns use **case-insensitive substring matching** on field coordinates.

### Matching Rules

```java
// Field coordinate: "user.emailAddress"
// Matches patterns with coordinate containing: "email" ✓

// Field coordinate: "customer.phoneNumber"
// Matches patterns with coordinate containing: "phone" ✓

// Field coordinate: "order.items[0].productName"
// Matches patterns with coordinate containing: "name" ✓
```

### Exact vs Partial Matching

```yaml
# This pattern
- coordinate: email
  value: "[a-z]+@test.com"
  type: regex

# Matches all these fields:
# - email
# - userEmail
# - emailAddress
# - customer.email
# - contact.emailPrimary
```

For more specific matching:

```yaml
# More specific pattern
- coordinate: user.email
  value: "[a-z]+@user.com"
  type: regex

# Only matches:
# - user.email
# - customer.user.email
# But NOT:
# - adminEmail (doesn't contain "user.email")
```

## Built-in Default Patterns

Matriarch includes standard patterns out of the box:

### Common Patterns

| Coordinate  | Pattern                                   | Type  | Example Output   |
|-------------|-------------------------------------------|-------|------------------|
| email       | `[a-z]{5,10}@(gmail\|outlook\|yahoo).com` | regex | test@gmail.com   |
| phoneNumber | `\\d{3}-\\d{3}-\\d{4}`                    | regex | 555-123-4567     |
| zipCode     | `\\d{5}`                                  | regex | 12345            |
| id          | `[A-Z]{3}-\\d{6}`                         | regex | ABC-123456       |
| userId      | `USER-\\d{8}`                             | regex | USER-12345678    |
| firstName   | Common names                              | list  | John, Jane, etc. |
| lastName    | Common surnames                           | list  | Smith, Doe, etc. |
| status      | `ACTIVE,INACTIVE,PENDING`                 | list  | ACTIVE           |
| priority    | `HIGH,MEDIUM,LOW`                         | list  | MEDIUM           |
| country     | `US,UK,CA,DE,FR`                          | list  | US               |

> 💡 See the source code for the complete list of built-in patterns.

## Complete Example

### patterns.yaml

```yaml
patterns:
  # Domain-specific email pattern
  - coordinate: email
    value: "[a-z]{5,10}@mycompany\\.com"
    type: regex

  # Order ID format
  - coordinate: orderId
    value: "ORD-\\d{8}"
    type: regex

  # Product SKU format
  - coordinate: sku
    value: "[A-Z]{3}-[0-9]{4}-[A-Z]{2}"
    type: regex

  # User roles
  - coordinate: user.role
    value: "CUSTOMER,ADMIN,SUPPORT,BILLING"
    type: list

  # Order status
  - coordinate: order.status
    value: "PENDING,PROCESSING,SHIPPED,DELIVERED,CANCELLED"
    type: list

  # Fixed test environment
  - coordinate: environment
    value: "TEST"
    type: string

  # Department codes
  - coordinate: departmentCode
    value: "\\d{4}"
    type: regex
```

### Usage in Tests

```java

@Test
void testWithPatterns() {
    // All patterns automatically applied
    User user = Mother.forClass(User.class).build();

    // user.email matches: [a-z]{5,10}@mycompany.com
    assertTrue(user.getEmail().endsWith("@mycompany.com"));

    // user.role is one of: CUSTOMER, ADMIN, SUPPORT, BILLING
    assertNotNull(user.getRole());
}

@Test
void testOrder() {
    Order order = Mother.forClass(Order.class).build();

    // order.orderId matches: ORD-\d{8}
    assertTrue(order.getOrderId().matches("ORD-\\d{8}"));

    // order.status is one of: PENDING, PROCESSING, etc.
    assertNotNull(order.getStatus());
}
```

### Override Patterns When Needed

Explicit overrides always take precedence:

```java

@Test
void testWithOverride() {
    // Pattern says email should be @mycompany.com
    // But we override it explicitly
    User user = Mother.forClass(User.class)
            .forField("email", "custom@different.com")
            .build();

    assertEquals("custom@different.com", user.getEmail());
}
```

## Precedence Examples

### Example 1: All Sources Defined

```yaml
# patterns.yaml
- coordinate: email
  value: "[a-z]+@yaml.com"
  type: regex
```

```bash
# JVM property
-Dmatriarch.pattern.email="regex:[a-z]+@jvm.com"
```

```java
// Built-in default: [a-z]+@(gmail|outlook).com

User user = Mother.forClass(User.class).build();
// Result: email ends with @jvm.com (JVM property wins)
```

### Example 2: Explicit Override

```bash
-Dmatriarch.pattern.email="regex:[a-z]+@jvm.com"
```

```java
User user = Mother.forClass(User.class)
        .forField("email", "explicit@override.com")  // Highest precedence
        .build();

// Result: "explicit@override.com"
```

### Example 3: Type-based vs Pattern

```yaml
# patterns.yaml
- coordinate: email
  value: "[a-z]+@pattern.com"
  type: regex
```

```java
User user = Mother.forClass(User.class)
        .forType(String.class, () -> "type-based-value")
        .build();

// Result: email uses pattern, not type-based
// Pattern matching has higher precedence than type-based generators
```

### Complete Precedence Order

```
1. .forField("field", value)           // Explicit field value
   ↓
2. .forField("field", supplier)        // Custom field generator
   ↓
3. .forType(Class, supplier)           // Type-based generator
   ↓
4. JVM System Properties               // -Dmatriarch.pattern.*
   ↓
5. Custom YAML Patterns                // patterns.yaml
   ↓
6. Built-in Default Patterns           // Matriarch defaults
   ↓
7. Random Generation                   // Fallback
```

## CI/CD Integration

### GitHub Actions

```yaml
name: Tests
on: [ push, pull_request ]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '17'

      - name: Run tests with patterns
        run: |
          mvn test \
            -Dmatriarch.pattern.email="regex:[a-z]+@test\\.com" \
            -Dmatriarch.pattern.environment="string:CI"
```

### GitLab CI

```yaml
test:
  script:
    - mvn test
      -Dmatriarch.pattern.email="regex:[a-z]+@test\\.com"
      -Dmatriarch.pattern.apiKey="regex:TEST-KEY-[A-Z0-9]{16}"
```

### Jenkins

```groovy
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh '''
                    mvn test \
                        -Dmatriarch.pattern.email="regex:[a-z]+@jenkins\\.com" \
                        -Dmatriarch.pattern.environment="string:JENKINS"
                '''
            }
        }
    }
}
```

## Environment-Specific Patterns

### Development

```yaml
# patterns-dev.yaml
patterns:
  - coordinate: email
    value: "[a-z]+@dev\\.local"
    type: regex
  - coordinate: apiEndpoint
    value: "http://localhost:8080"
    type: string
```

### Testing

```yaml
# patterns-test.yaml
patterns:
  - coordinate: email
    value: "[a-z]+@test\\.com"
    type: regex
  - coordinate: apiEndpoint
    value: "https://test.api.company.com"
    type: string
```

### CI/CD

Use system properties to override:

```bash
# In CI pipeline
mvn test -Dmatriarch.pattern.email="regex:ci-[a-z]+@test\\.com"
```

## Regex Pattern Guide

Matriarch supports a subset of regex syntax for pattern generation:

### Character Classes

```yaml
- coordinate: lowercase
  value: "[a-z]{5}"          # 5 lowercase letters
  type: regex

- coordinate: uppercase
  value: "[A-Z]{3}"          # 3 uppercase letters
  type: regex

- coordinate: digits
  value: "[0-9]{4}"          # 4 digits
  type: regex

- coordinate: mixed
  value: "[a-zA-Z0-9]{8}"    # 8 alphanumeric chars
  type: regex

- coordinate: custom
  value: "[abc123]{6}"       # 6 chars from set
  type: regex
```

### Quantifiers

```yaml
- coordinate: exact
  value: "[a-z]{5}"          # Exactly 5 chars
  type: regex

- coordinate: range
  value: "[a-z]{5,10}"       # 5 to 10 chars
  type: regex

- coordinate: optional
  value: "[a-z]?"            # 0 or 1 char
  type: regex

- coordinate: oneOrMore
  value: "[a-z]+"            # 1 or more chars
  type: regex

- coordinate: zeroOrMore
  value: "[a-z]*"            # 0 or more chars
  type: regex
```

### Groups and Alternation

```yaml
- coordinate: domain
  value: "(gmail|outlook|yahoo)"
  type: regex

- coordinate: email
  value: "[a-z]{5}@(gmail|outlook)\\.com"
  type: regex

- coordinate: phone
  value: "(555|123|777)-\\d{3}-\\d{4}"
  type: regex
```

### Escape Sequences

```yaml
- coordinate: digit
  value: "\\d{4}"            # \d = digit [0-9]
  type: regex

- coordinate: word
  value: "\\w{8}"            # \w = word char [a-zA-Z0-9_]
  type: regex

- coordinate: space
  value: "\\s+"              # \s = whitespace
  type: regex

- coordinate: literal
  value: "test\\.com"        # \. = literal dot
  type: regex
```

### Complex Patterns

```yaml
- coordinate: email
  value: "[a-z]{5,10}@(gmail|yahoo|outlook)\\.(com|net|org)"
  type: regex
  # Example: hello@gmail.com, test@yahoo.net

- coordinate: phoneNumber
  value: "\\+1-\\d{3}-\\d{3}-\\d{4}"
  type: regex
  # Example: +1-555-123-4567

- coordinate: orderId
  value: "ORD-(202[3-5])-\\d{6}"
  type: regex
  # Example: ORD-2024-123456

- coordinate: sku
  value: "[A-Z]{3}-[0-9]{4}-[A-Z]{2}"
  type: regex
  # Example: ABC-1234-XY
```

## Troubleshooting

### Pattern Not Applied

**Issue:** Field isn't using the expected pattern.

**Solutions:**

1. Check coordinate matching:

```yaml
# Too specific
- coordinate: user.customer.email  # Won't match "email" field

# Better
- coordinate: email  # Matches any field containing "email"
```

2. Verify precedence:

```bash
# Check if JVM property is overriding
echo $MAVEN_OPTS  # Look for -Dmatriarch.pattern.*
```

3. Confirm file location:

```
src/test/resources/patterns.yaml  # ✓ Correct
src/main/resources/patterns.yaml  # ✗ Wrong location
```

### Regex Not Generating

**Issue:** Regex pattern not producing expected values.

**Solutions:**

1. Escape special characters:

```yaml
# Wrong
value: "test.com"  # Dot matches any character

# Correct
value: "test\\.com"  # Escaped dot
```

2. Check type specification:

```yaml
# Wrong
value: "[a-z]{5}"
type: string  # Treats as literal string

# Correct
value: "[a-z]{5}"
type: regex  # Generates from pattern
```

### YAML Syntax Errors

```yaml
# Wrong - missing quotes
- coordinate: email
  value: [ a-z ]+@test.com
  type: regex

# Correct - quotes around value
- coordinate: email
  value: "[a-z]+@test.com"
  type: regex
```

## Best Practices

### 1. Use Specific Coordinates

```yaml
# Good - specific
- coordinate: user.email
  value: "[a-z]+@user.com"
  type: regex

- coordinate: admin.email
  value: "[a-z]+@admin.com"
  type: regex

# Less specific - applies broadly
- coordinate: email
  value: "[a-z]+@generic.com"
  type: regex
```

### 2. Document Your Patterns

```yaml
patterns:
  # Customer email addresses - always @customer.com domain
  - coordinate: customer.email
    value: "[a-z]{5,10}@customer\\.com"
    type: regex

  # Order IDs - format: ORD-YYYYMMDD-NNNNNN
  - coordinate: orderId
    value: "ORD-20[2-9][0-9][0-1][0-9][0-3][0-9]-\\d{6}"
    type: regex
```

### 3. Use JVM Properties for Environments

```bash
# Development
mvn test -Dmatriarch.pattern.environment="string:DEV"

# CI
mvn test -Dmatriarch.pattern.environment="string:CI"

# Production-like tests
mvn test -Dmatriarch.pattern.environment="string:PROD"
```

### 4. Keep YAML Patterns Domain-Specific

```yaml
# patterns.yaml - domain defaults
patterns:
  - coordinate: customerId
    value: "CUST-\\d{8}"
    type: regex

  - coordinate: product.category
    value: "ELECTRONICS,CLOTHING,FOOD,BOOKS"
    type: list
```

## Next Steps

- **[Advanced Usage]({{< ref "advanced-usage.md" >}})** - Complex scenarios and custom logic
- **[Examples]({{< ref "examples.md" >}})** - Real-world pattern configurations
- **[API Reference]({{< ref "api-reference.md" >}})** - Complete API documentation

