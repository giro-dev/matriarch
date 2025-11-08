![logo_90.png](documentation/logo_90.png)

Matriarch is a library to help your testing process. It generates objects with random values in a easy way,
bringing you the chance to force values to be placed in defined fields.

## Features

✨ **Flexible Object Generation** - Create fully populated test objects with random or specified values  
🎯 **Smart Overrides** - Override specific fields using dot notation for nested objects  
🔤 **Regex Pattern Support** - Generate data matching regex patterns with built-in generator  
📝 **Builder Pattern** - Fluent, readable API for test data creation  
🧪 **JUnit Integration** - Seamless integration with JUnit ParameterizedTest  
📊 **Named Test Cases** - Add descriptive names for better test reporting  
🎲 **Multiple Strategies** - Use as builder, factory, or JUnit annotation  
⚡ **Lightweight** - No heavy dependencies, fast test execution  
📚 **Well Documented** - Comprehensive guides and examples

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

// Custom generators for specific fields
User user = Mother.forClass(User.class)
    .forField("email", () -> faker.internet().emailAddress())
    .forField("birthDate", () -> LocalDate.now().minusYears(25))
    .build();

// Type-based generators for all fields of a type
Order order = Mother.forClass(Order.class)
    .forType(LocalDate.class, () -> LocalDate.now().minusDays(30))
    .forType(BigDecimal.class, () -> BigDecimal.valueOf(random.nextDouble() * 1000))
    .build();

// Collections and stream generation
List<User>     list = Mother.forClass(User.class).buildList(10);
Set<User>       set = Mother.forClass(User.class).buildSet(10);
Stream<User> stream = Mother.forClass(User.class).buildStream(10);

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




### Set up
Add the dependency to your project
#### Maven

```xml

<dependency>
    <groupId>dev.agiro</groupId>
    <artifactId>matriarch</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```
#### Gradle 
```kotlin
    testImplementation("dev.agiro:matriarch:0.1.0")
```

### Usage
The matriarch objetMother generator has three ways to use it. 
Most useful is as [builder](#as-builder), but also could be used as a [field instance](#field-instance-used-as-factory) or as a [Junit ParameterizedTest](#as-annotation-for-junit-parameterized-test) to inject parameters to your test

### As Builder.

This way you can use the `Mother` as a static method. It receives a Map<String, Overrider> with the coordinates and values that you want to
override.

If you only want a random object of `SomeType`, it is as simple as:

```java
var randomSomeType = Mother.forClass(SomeType.class).build;
```
If you need to define some specific fields for your use case, could be defines as follow:

```java
var randomSomeType = Mother.forClass(SomeType.class)
        .forField("sStringValue", "my String Value")
        .forField("otherStringValue", "A-\\d{4}(-V\\d+)?", Overrider.OverriderType.REGEX)
        .forField("anObjectValue", new OtherObject())
        .build;
```

> 💡 **Note**: The method `.override()` is still available as an alias for `.forField()` for backward compatibility, but `.forField()` is the recommended method as it's more explicit about its purpose.

There is several ways to define your field values, a more detailed options are listed below:

```java
    import java.time.Instant;

var claimCategorizedEvent = Mother.builder(PurchaseAggregate.class)
        /* 1.i */.forField("aggregate.user.valid", Overrider.with(techWarrantyFlag.toString()))
        /* 1.ii */.forField("aggregate.user.role", "STANDARD")
        /* 1.iii */.forField("aggregate.user.address", Overrider.with("C/Mississippi 56, 8B"))
        /* 2.i */.forField("aggregate.user.objectId", Overrider.regex("A\\d{10}"))
        /* 2.ii */.forField("aggregate.user.objectType", "\\d{4}", Overrider.OverriderType.REGEX)
        /* 3.i */.forField("aggregate.user.objectSubType", Overrider.object(new PartEs2("1234")))
        /* 3.ii */.forField("aggregate.purchase.date", Instant.now())
        /* 4.i */.forField("aggregate.purchase.claimDate", Overrider.nullValue())
        /* 4.ii */.forField("aggregate.purchase.voucher", null)
        /* 5.i */.forField("aggregate.list[3].date", Instant.now())
        /* 5.ii */.forField("aggregate.map[thomas].date", Instant.now())
        .build();
 ```

there are two types of field value setters:

1. Overrider.with(String): produces String or try to generate and object. it receives a value that you want to set. Could be used as:
    1. `.forField("aggregate.user.valid", Overrider.with(techWarrantyFlag.toString()))`
    2. `.forField("aggregate.user.role", "STANDARD")`
    3. `.forField("aggregate.user.address", Overrider.with("C/Mississippi 56, 8B"))`
2. Overrider.regex(String): it receives a regex pattern that you want to set. Cold be used as:
   1. `.forField("aggregate.user.objectId", Overrider.regex("A\\d{10}"))`
   2. `.forField("aggregate.user.objectType", "\\d{4}", Overrider.OverriderType.REGEX)`
3. Overrider.object(Object): it receives an object that you want to set. Cold be used as:
   1. `.forField("aggregate.user.objectSubType", Overrider.object(new PartEs2("1234")))`
   2. `.forField("aggregate.purchase.date", Instant.now())`
4. Overrider.nullValue(): it will set the field as null. Could be used as:
   1. `.forField("aggregate.purchase.claimDate", Overrider.nullValue())`
   2. `.forField("aggregate.purchase.voucher", null)`
5. You can Override a List element using `[<index>]` and Map value using `[<key>]`
   1. by default the list size is random between 1 and 15. If you specify an index bigger than that, the list will be filled as this index (randomly in cases where no override definition exists)
   2. Map objects are limited at the moment to a Map<Object, Object>, it will try to generate:
     - A **key** with the `<key>` to the object defined as key 
     - A **value** as defined.  !is not allowed to generate random values for Map or Collection (if you need it you have to pass them as a Overrider.object(<CollectionValue>))     

#### Custom Field Generators with forField

Use `.forField(String fieldName, Supplier<?> generator)` to provide a custom generator for a specific field:

```java
Mother.forClass(User.class)
    .forField("email", () -> faker.internet().emailAddress())
    .forField("birthDate", () -> LocalDate.now().minusYears(25))
    .forField("accountId", () -> UUID.randomUUID().toString())
    .build();
```

**Use cases:**
- Dynamic values based on external data sources
- Complex logic for field generation
- Integration with other libraries (Faker, RandomUtils, etc.)
- Values that depend on runtime conditions

**Example with business logic:**
```java
Mother.forClass(Order.class)
    .forField("orderId", () -> "ORD-" + System.currentTimeMillis())
    .forField("price", () -> BigDecimal.valueOf(random.nextDouble() * 100).setScale(2, RoundingMode.HALF_UP))
    .forField("status", () -> random.nextBoolean() ? "PENDING" : "COMPLETED")
    .build();
```

#### Type-based Generators with forType

Use `.forType(Class<T> type, Supplier<T> generator)` to provide a custom generator for **all fields** of a specific type:

```java
Mother.forClass(Order.class)
    .forType(LocalDate.class, () -> LocalDate.now().minusDays(random.nextInt(365)))
    .forType(BigDecimal.class, () -> BigDecimal.valueOf(random.nextDouble() * 1000))
    .forType(UUID.class, () -> UUID.randomUUID())
    .build();
```

**Use cases:**
- Consistent generation across all fields of a type
- Date/time values within specific ranges
- Custom domain objects with specific constraints
- Preventing null values for specific types

**Example with date ranges:**
```java
Mother.forClass(Invoice.class)
    // All LocalDate fields will be in the past year
    .forType(LocalDate.class, () -> LocalDate.now().minusDays(random.nextInt(365)))
    // All LocalDateTime fields will be recent
    .forType(LocalDateTime.class, () -> LocalDateTime.now().minusHours(random.nextInt(24)))
    .build();
```

#### Combining forType and forField

Field-specific generators take precedence over type-based generators:

```java
Mother.forClass(Invoice.class)
    // All LocalDate fields will use this by default
    .forType(LocalDate.class, () -> LocalDate.now().minusDays(30))
    
    // Except these specific fields which override the type generator
    .forField("dueDate", () -> LocalDate.now().plusDays(30))
    .forField("paymentDate", () -> LocalDate.now().plusDays(60))
    
    .build();
```

**Precedence order (highest to lowest):**
1. `.forField(field, value)` - Explicit field value (also available as `.override()` for compatibility)
2. `.forField(field, supplier)` - Custom field generator
3. `.forType(class, supplier)` - Type-based generator
4. Pattern matching (YAML/ENV/default)
5. Random generation

#### Collection Size Configuration

Use `.withCollectionSize(int min, int max)` to control the size of generated collections (Lists, Sets):

```java
Mother.forClass(Order.class)
    .withCollectionSize(3, 10)  // Collections will have 3-10 elements
    .build();
```

**Default behavior:** Collections have 1-15 elements randomly.

**Example with specific sizes:**
```java
// Generate orders with exactly 5 items
Mother.forClass(Order.class)
    .withCollectionSize(5, 5)
    .build();

// Generate users with 0-3 addresses (including empty)
Mother.forClass(User.class)
    .withCollectionSize(0, 3)
    .build();
```

#### Excluding Fields

Use `.excludeFields(String... fields)` to skip generation for specific fields (they will remain null):

```java
Mother.forClass(User.class)
    .excludeFields("password", "internalId", "metadata")
    .build();
```

**Use cases:**
- Sensitive fields that shouldn't be populated in tests
- Fields that cause issues during generation
- Optional fields not needed for specific test scenarios
- Fields with complex dependencies

**Example:**
```java
Mother.forClass(Account.class)
    .excludeFields("encryptedPassword", "securityToken")
    .forField("username", "testuser")
    .build();
// password and securityToken will be null
```     

### As Annotation for Junit Parameterized Test
Add a `@MotherFactoryResource` annotation to define test parameters with ease.

#### Basic Usage

```java
@ParameterizedTest
@MotherFactoryResource(args = {
    @RandomArg(targetClass = User.class)
})
void testWithRandomUser(User user) {
    assertNotNull(user);
}
```

#### Named Test Cases (NEW!)
Add descriptive names to your test cases for better test reporting:

```java
@ParameterizedTest(name = "[{index}] {0}")
@MotherFactoryResource(args = {
    @RandomArg(
        name = "Valid User",
        targetClass = User.class,
        overrides = @OverrideField(field = "email", value = "valid@example.com")
    ),
    @RandomArg(
        name = "Admin User",
        targetClass = User.class,
        overrides = @OverrideField(field = "role", value = "ADMIN")
    )
})
void testUsers(User user) {
    // Each named test case will appear in test reports
}
```

#### Regex Pattern Support (NEW!)
Generate random data matching regex patterns:

```java
@ParameterizedTest
@MotherFactoryResource(args = {
    @RandomArg(
        targetClass = User.class,
        overrides = {
            @OverrideField(
                field = "email", 
                value = "[a-z]{5,10}@(gmail|outlook).com",
                isRegex = true  // NEW: Direct regex support
            ),
            @OverrideField(
                field = "phoneNumber",
                value = "\\d{3}-\\d{3}-\\d{4}",
                isRegex = true
            )
        }
    )
})
void testWithRegexPatterns(User user) {
    assertTrue(user.getEmail().matches(".*@(gmail|outlook)\\.com"));
}
```

#### JSON Overrides
For complex nested structures, use JSON format:

```java
@ParameterizedTest
@MotherFactoryResource(args = {
    @RandomArg(
        targetClass = CreatePurchaseCommand.class,
        jsonOverrides = """
            {
                "version": 3,
                "partRequestList": [
                    {"part": "PART1"},
                    {"part": "PART2"}
                ],
                "user": {
                    "email": "test@example.com",
                    "role": "ADMIN"
                }
            }
            """,
        overrides = {
            @OverrideField(field = "actionId", value = "PF-[AZ]{3}\\d{4}-V\\d", isRegex = true),
            @OverrideField(field = "claim.value", value = "CLA-[AZ]{3}\\d{4}-V\\d", isRegex = true)
        }
    )
})
void test(CreatePurchaseCommand command) {
    assertEquals(3, command.getVersion());
    assertEquals(2, command.getPartRequestList().size());
}
```

#### Multiple Test Cases
Generate multiple test cases in a single annotation:

```java
@ParameterizedTest(name = "Testing: {0}")
@MotherFactoryResource(args = {
    @RandomArg(name = "Case 1", targetClass = User.class),
    @RandomArg(name = "Case 2", targetClass = User.class),
    @RandomArg(name = "Case 3", targetClass = User.class)
})
void testMultipleCases(User user) {
    // Test runs 3 times with different random data
}
```

#### Annotation Reference

**@MotherFactoryResource**
- `args`: Array of `@RandomArg` - each represents one test case

**@RandomArg**
- `name` (optional): Descriptive name for test reports (NEW!)
- `targetClass`: The class type to generate
- `overrides` (optional): Array of `@OverrideField` for field-level overrides
- `jsonOverrides` (optional): JSON string for complex nested overrides

**@OverrideField**
- `field`: Field name (supports dot notation for nested: "address.city")
- `value`: Value to set or regex pattern
- `type`: Override type (`STRING`, `OBJECT`, `REGEX`, `NULL`)
- `isRegex` (optional): If true, treats value as regex pattern (NEW!)

> 📝 **Note**: In the annotation context, it's called `@OverrideField` for clarity in JUnit tests. In the builder API, use `.forField()` which is the recommended method (`.override()` is still available as an alias for backward compatibility).

> 📚 For more examples and advanced usage, see [PARAMETERIZED_TESTING_GUIDE.md](PARAMETERIZED_TESTING_GUIDE.md)
### Field Instance used as Factory.

this way is not recommended, but it is possible to use the `Mother` as a field. it receives a Map<String, Overrider> with the coordinates
and values that you want to override.

```java
    class PartDataCreatorTest extends UnitTestCase {

    Mother<User> flashDataMother = new Mother<>(User.class);

    @Test
    void test() {
        //Using a field declared objectMother to create the object
        final User user = flashDataMother.create(Map.of("name.value", Overrider.with("John"),
                                                                  "height.value", Overrider.with("193.5")));
    }
}
```

## Known Pattern Values

Matriarch supports pattern-based value generation from multiple sources, which are automatically applied when field coordinates match the pattern coordinate.

### Pattern Sources (in order of precedence)

#### 1. Custom YAML Patterns
Create a `patterns.yaml` file in `src/test/resources` to define your own patterns:

```yaml
patterns:
  - coordinate: user.sub
    value: USER_ID_\d{5}
    type: regex
  - coordinate: user.role
    value: ADMIN,STANDARD,VISITOR
    type: list
  - coordinate: email
    value: "[a-z]{5,10}@(gmail|outlook).com"
    type: regex
  - coordinate: plantId
    value: \d{4}
    type: regex
```

**Supported types:**
- `string` (default) - Returns the exact value
- `regex` - Generates a random string matching the regex pattern
- `list` - Randomly selects one value from comma-separated list

#### 2. Default Built-in Patterns
Matriarch includes 12 standard patterns that work out of the box:

| Coordinate | Pattern | Example Output |
|------------|---------|----------------|
| `email` | `[a-z]{4,8}\.[a-z]{4,8}_[a-z]{4,8}@(gmail\|outlook).com` | `test.user_demo@gmail.com` |
| `phoneNumber` | `\d{3}-\d{3}-\d{4}` | `555-123-4567` |
| `username` | `[a-z]{5,10}` | `johndoe` |
| `userId` | `USER_\d{6}` | `USER_123456` |
| `id` | `\d{8}` | `12345678` |
| ... | ... | ... |

> 💡 **Tip**: Custom YAML patterns override default patterns for the same coordinate.

#### 3. Environment Variable Patterns
Set patterns dynamically using environment variables:

```bash
# Set a pattern via environment variable
export MATRIARCH_PATTERN_API_KEY="API-[A-Z]{3}-\d{6}"
export MATRIARCH_PATTERN_USER_EMAIL="test@mycompany.com"
```

Then use the coordinate in your tests:
```java
User user = Mother.forClass(User.class)
    .forField("apiKey", "API_KEY")  // Will use env var pattern
    .build();
```

> 🔒 **Use Case**: Environment variables are useful for sensitive data like API keys, database URLs, or environment-specific values in CI/CD pipelines.

### How Pattern Matching Works

Patterns are matched using **case-insensitive substring matching** on field coordinates:

```java
// Field: user.email.address
// Matches pattern coordinate: "email" ✓

// Field: customer.phoneNumber
// Matches pattern coordinate: "phoneNumber" ✓

// Field: product.name
// Matches pattern coordinate: "name" ✓
```

### Pattern Precedence

When multiple patterns could match, they are applied in this order:
1. **Explicit overrides** in your code (highest priority)
2. **Custom YAML patterns** from `patterns.yaml`
3. **Environment variable patterns**
4. **Default built-in patterns** (lowest priority)

Example:
```java
// If patterns.yaml has: email → "[a-z]+@custom.com"
// And ENV has: MATRIARCH_PATTERN_EMAIL="env@email.com"
// And default has: email → "[a-z]+@(gmail|outlook).com"

// This will use the YAML pattern (highest priority after explicit field value)
User user = Mother.forClass(User.class).build();
// user.email → "test@custom.com"

// This explicit field value takes precedence over all patterns
User user2 = Mother.forClass(User.class)
    .forField("email", "explicit@override.com")
    .build();
// user2.email → "explicit@override.com"
```

### Regex Pattern Support
Matriarch includes a built-in lightweight regex generator that supports:
- **Character classes**: `[a-z]`, `[A-Z]`, `[0-9]`, `[abc]`
- **Quantifiers**: `{n}`, `{n,m}`, `*`, `+`, `?`
- **Groups with alternation**: `(option1|option2|option3)`
- **Escaped characters**: `\d` (digit), `\w` (word char), `\s` (space)
- **Nested patterns**: Complex combinations of the above

Examples:
- `\d{4}` → generates "1234", "5678", etc.
- `[a-z]{5,8}@gmail.com` → generates "hello@gmail.com", "test@gmail.com", etc.
- `(gmail|outlook|yahoo)` → generates either "gmail", "outlook", or "yahoo"
- `\d{3}-\d{3}-\d{4}` → generates "555-123-4567", etc.

## Recent Improvements

### v0.5.1 (November 2025)
- ✨ **Named Test Cases**: Add descriptive names to parameterized tests for better reporting
- ✨ **Direct Regex Support**: Use `isRegex = true` in `@OverrideField` for easy regex patterns
- ✨ **Enhanced Error Messages**: Clear, actionable errors that show exactly what went wrong
- ✨ **Multiple Test Cases**: Easy support for generating many test cases from a single annotation
- ⚡ **Lightweight**: Removed external datafaker dependency in favor of built-in regex generator
- 📚 **Better Documentation**: Comprehensive guides and examples for all features


## Additional Resources
- 📝 **[Example Tests](src/test/java/dev/agiro/matriarch/ParameterizedTestExample.java)** - Working examples

# Thanks to Opensource
This library uses the following opensource projects:
 - https://github.com/FasterXML/jackson

Note: Previously used datafaker for regex generation, but now includes a lightweight custom regex generator for better performance and reduced dependencies.

