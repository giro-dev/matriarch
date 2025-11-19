---
title: "API Reference"
date: 2025-11-15
draft: false
weight: 9
---

# API Reference

Complete reference for all Matriarch APIs, classes, and annotations.

## Core Classes

### Mother&lt;T&gt;

The main builder class for generating test objects.

#### Static Factory Method

```java
public static <T> Mother<T> forClass(Class<T> targetClass)
```

Creates a new builder instance for the specified class.

**Parameters:**
- `targetClass` - The class type to generate

**Returns:** A new `Mother<T>` builder instance

**Example:**
```java
Mother<User> builder = Mother.forClass(User.class);
```

#### Instance Methods

##### forField()

```java
public Mother<T> forField(String fieldPath, Object value)
```

Overrides a specific field with the given value.

**Parameters:**
- `fieldPath` - Field path (supports dot notation and collection indexing)
- `value` - Value to set (String, Object, or null)

**Returns:** This builder instance for chaining

**Example:**
```java
Mother.forClass(User.class)
    .forField("email", "test@example.com")
    .forField("age", 30)
    .forField("address.city", "New York")
    .build();
```

---

```java
public Mother<T> forField(String fieldPath, String pattern, Overrider.OverriderType type)
```

Overrides a field with a pattern of the specified type.

**Parameters:**
- `fieldPath` - Field path
- `pattern` - Pattern string (for REGEX type)
- `type` - Type of override (STRING, REGEX, NULL, OBJECT)

**Returns:** This builder instance

**Example:**
```java
Mother.forClass(User.class)
    .forField("email", "[a-z]+@test\\.com", Overrider.OverriderType.REGEX)
    .build();
```

---

```java
public Mother<T> forField(String fieldPath, Supplier<?> generator)
```

Provides a custom generator for a specific field.

**Parameters:**
- `fieldPath` - Field path
- `generator` - Supplier function that generates the value

**Returns:** This builder instance

**Example:**
```java
Mother.forClass(User.class)
    .forField("email", () -> faker.internet().emailAddress())
    .forField("createdAt", () -> Instant.now())
    .build();
```

##### override()

```java
public Mother<T> override(String fieldPath, Object value)
```

Legacy alias for `forField()`. Provided for backward compatibility.

**Parameters:** Same as `forField()`

**Returns:** This builder instance

**Example:**
```java
Mother.forClass(User.class)
    .override("email", "test@example.com")  // Same as .forField()
    .build();
```

> 💡 **Recommendation:** Use `.forField()` for new code.

##### forType()

```java
public Mother<T> forType(Class<?> type, Supplier<?> generator)
```

Provides a custom generator for all fields of a specific type.

**Parameters:**
- `type` - The class type to match
- `generator` - Supplier function that generates values

**Returns:** This builder instance

**Example:**
```java
Mother.forClass(Order.class)
    .forType(LocalDate.class, () -> LocalDate.now().minusDays(30))
    .forType(BigDecimal.class, () -> BigDecimal.valueOf(100.00))
    .build();
```

##### withCollectionSize()

```java
public Mother<T> withCollectionSize(int min, int max)
```

Sets the size range for generated collections.

**Parameters:**
- `min` - Minimum collection size (inclusive)
- `max` - Maximum collection size (inclusive)

**Returns:** This builder instance

**Example:**
```java
Mother.forClass(Order.class)
    .withCollectionSize(3, 10)  // Collections: 3-10 elements
    .build();
```

**Default:** If not specified, collections have 1-15 elements randomly.

##### excludeFields()

```java
public Mother<T> excludeFields(String... fieldPaths)
```

Excludes specified fields from generation (they remain null).

**Parameters:**
- `fieldPaths` - Varargs of field paths to exclude

**Returns:** This builder instance

**Example:**
```java
Mother.forClass(User.class)
    .excludeFields("password", "securityToken", "metadata")
    .build();
```

##### build()

```java
public T build()
```

Generates and returns a single instance.

**Returns:** A populated instance of type T

**Example:**
```java
User user = Mother.forClass(User.class)
    .forField("email", "test@example.com")
    .build();
```

##### buildList()

```java
public List<T> buildList(int count)
```

Generates and returns a list of instances.

**Parameters:**
- `count` - Number of instances to generate

**Returns:** A list of populated instances

**Example:**
```java
List<User> users = Mother.forClass(User.class)
    .forField("role", "STANDARD")
    .buildList(10);
```

##### buildSet()

```java
public Set<T> buildSet(int count)
```

Generates and returns a set of instances.

**Parameters:**
- `count` - Number of instances to generate

**Returns:** A set of populated instances

**Example:**
```java
Set<User> users = Mother.forClass(User.class)
    .buildSet(5);
```

##### buildStream()

```java
public Stream<T> buildStream(int count)
```

Generates and returns a stream of instances (lazy evaluation).

**Parameters:**
- `count` - Number of instances to generate

**Returns:** A stream of populated instances

**Example:**
```java
Stream<User> users = Mother.forClass(User.class)
    .buildStream(1000);

long activeCount = users
    .filter(User::isActive)
    .count();
```

### Overrider

Utility class for creating field overrides.

#### Static Methods

```java
public static Overrider with(String value)
```

Creates a string value override.

**Example:**
```java
Overrider.with("test@example.com")
```

---

```java
public static Overrider regex(String pattern)
```

Creates a regex pattern override.

**Example:**
```java
Overrider.regex("[a-z]{5}@test\\.com")
```

---

```java
public static Overrider object(Object value)
```

Creates an object value override.

**Example:**
```java
Overrider.object(new Address("123 Main St", "New York"))
```

---

```java
public static Overrider nullValue()
```

Creates a null value override.

**Example:**
```java
Overrider.nullValue()
```

#### Enum: OverriderType

```java
public enum OverriderType {
    STRING,   // Direct string value
    REGEX,    // Regex pattern
    OBJECT,   // Direct object
    NULL      // Null value
}
```

### Regex

Wrapper class for regex patterns (primarily for annotations).

#### Constructor

```java
public Regex(String pattern)
```

Creates a regex pattern wrapper.

**Example:**
```java
new Regex("[a-z]{5,10}@gmail\\.com")
```

## JUnit Annotations

### @MotherFactoryResource

Annotation for parameterized tests.

#### Attributes

```java
RandomArg[] args()
```

Array of `@RandomArg` annotations defining test cases.

**Example:**
```java
@ParameterizedTest
@MotherFactoryResource(args = {
    @RandomArg(targetClass = User.class),
    @RandomArg(targetClass = User.class, overrides = {...})
})
void test(User user) { ... }
```

### @RandomArg

Defines a single test case for `@MotherFactoryResource`.

#### Attributes

```java
String name() default ""
```

Descriptive name for test reports.

**Example:**
```java
@RandomArg(name = "Admin User", targetClass = User.class)
```

---

```java
Class<?> targetClass()
```

**Required.** The class type to generate.

**Example:**
```java
@RandomArg(targetClass = User.class)
```

---

```java
OverrideField[] overrides() default {}
```

Array of field overrides.

**Example:**
```java
@RandomArg(
    targetClass = User.class,
    overrides = {
        @OverrideField(field = "email", value = "test@example.com"),
        @OverrideField(field = "role", value = "ADMIN")
    }
)
```

---

```java
String jsonOverrides() default ""
```

JSON string for complex nested structures.

**Example:**
```java
@RandomArg(
    targetClass = Order.class,
    jsonOverrides = """
        {
            "orderId": "ORD-123",
            "customer": {
                "email": "test@example.com"
            }
        }
        """
)
```

### @OverrideField

Defines a field override.

#### Attributes

```java
String field()
```

**Required.** Field path (supports dot notation).

**Example:**
```java
@OverrideField(field = "address.city", value = "New York")
```

---

```java
String value() default ""
```

Value to set or regex pattern.

**Example:**
```java
@OverrideField(field = "email", value = "test@example.com")
```

---

```java
OverrideType type() default OverrideType.STRING
```

Type of override.

**Example:**
```java
@OverrideField(field = "email", value = "[a-z]+@test\\.com", type = OverrideType.REGEX)
```

---

```java
boolean isRegex() default false
```

Shorthand for `type = OverrideType.REGEX`.

**Example:**
```java
@OverrideField(field = "email", value = "[a-z]+@test\\.com", isRegex = true)
```

#### Enum: OverrideType

```java
public enum OverrideType {
    STRING,   // Direct string value
    REGEX,    // Regex pattern
    OBJECT,   // Direct object (not common in annotations)
    NULL      // Null value
}
```

### @MotherConfig

Class-level annotation to enable field and parameter injection.

#### Attributes

```java
int collectionMin() default 1
```

Minimum size for generated collections.

**Example:**
```java
@MotherConfig(collectionMin = 5)
```

---

```java
int collectionMax() default 15
```

Maximum size for generated collections.

**Example:**
```java
@MotherConfig(collectionMax = 10)
```

---

```java
boolean jsonOverridesWin() default true
```

Whether JSON overrides take precedence over array overrides.

**Example:**
```java
@MotherConfig(jsonOverridesWin = false)
```

---

```java
long seed() default 0L
```

Reserved for future deterministic generation.

---

```java
String knownPatterns() default ""
```

**Not yet implemented.** Use `patterns.yaml` instead.

**Example:**
```java
@MotherConfig(collectionMin = 5, collectionMax = 10)
class MyTest {
    @Mother
    User user;
    
    @Test
    void test() { ... }
}
```

### @Mother

Marks fields and parameters for automatic injection.

#### Attributes

```java
OverrideField[] overrides() default {}
```

Array of field overrides.

**Example:**
```java
@Mother(overrides = {
    @OverrideField(field = "email", value = "test@example.com")
})
User user;
```

---

```java
String jsonOverrides() default ""
```

JSON string for complex structures.

**Example:**
```java
@Mother(jsonOverrides = """
    {
        "email": "test@example.com",
        "role": "ADMIN"
    }
    """)
User admin;
```

**Usage:**
```java
@MotherConfig
class MyTest {
    @Mother
    User defaultUser;
    
    @Mother(overrides = @OverrideField(field = "role", value = "ADMIN"))
    User adminUser;
    
    @Test
    void test1() {
        // Fields populated before each test
        assertNotNull(defaultUser);
        assertEquals("ADMIN", adminUser.getRole());
    }
    
    @Test
    void test2(@Mother User paramUser) {
        // Parameters injected
        assertNotNull(paramUser);
    }
}
```

## Pattern System

### patterns.yaml Format

```yaml
patterns:
  - coordinate: <field-coordinate>
    value: <pattern-or-value>
    type: <string|regex|list>
```

#### Fields

**coordinate** (string, required)
- Field coordinate to match (case-insensitive substring matching)
- Examples: `"email"`, `"user.email"`, `"phoneNumber"`

**value** (string, required)
- For `string` type: Exact value
- For `regex` type: Regex pattern
- For `list` type: Comma-separated values

**type** (string, required)
- `string` - Returns exact value
- `regex` - Generates from regex pattern
- `list` - Randomly selects from comma-separated list

#### Example

```yaml
patterns:
  - coordinate: email
    value: "[a-z]{5,10}@test\\.com"
    type: regex
  
  - coordinate: role
    value: "ADMIN,USER,GUEST"
    type: list
  
  - coordinate: environment
    value: "TEST"
    type: string
```

### JVM System Properties

Format:
```bash
-Dmatriarch.pattern.<coordinate>="<type>:<value>"
```

Or default to regex:
```bash
-Dmatriarch.pattern.<coordinate>="<value>"
```

**Examples:**
```bash
-Dmatriarch.pattern.email="regex:[a-z]+@test\\.com"
-Dmatriarch.pattern.role="list:ADMIN,USER,GUEST"
-Dmatriarch.pattern.environment="string:TEST"
```

## Supported Types

### Primitive Types

| Type | Generation Strategy |
|------|-------------------|
| `byte`, `Byte` | Random byte |
| `short`, `Short` | Random short |
| `int`, `Integer` | Random int (0-1000) |
| `long`, `Long` | Random long |
| `float`, `Float` | Random float (0.0-1000.0) |
| `double`, `Double` | Random double (0.0-1000.0) |
| `boolean`, `Boolean` | Random true/false |
| `char`, `Character` | Random alphanumeric |

### String Types

| Type | Generation Strategy |
|------|-------------------|
| `String` | Random alphanumeric (10-20 chars) |

### Date/Time Types

| Type | Generation Strategy |
|------|-------------------|
| `LocalDate` | Random date (±365 days from now) |
| `LocalTime` | Random time |
| `LocalDateTime` | Random datetime (±365 days from now) |
| `Instant` | Random instant (±365 days from now) |
| `ZonedDateTime` | Random zoned datetime |
| `OffsetDateTime` | Random offset datetime |

### Other Types

| Type | Generation Strategy |
|------|-------------------|
| `UUID` | Random UUID |
| `BigDecimal` | Random decimal (0.0-1000.0) |
| `BigInteger` | Random big integer |
| `Enum` | Random enum constant |

### Collection Types

| Type | Generation Strategy |
|------|-------------------|
| `List<T>` | List with 1-15 elements (configurable) |
| `Set<T>` | Set with 1-15 elements (configurable) |
| `ArrayList<T>` | ArrayList implementation |
| `LinkedList<T>` | LinkedList implementation |
| `HashSet<T>` | HashSet implementation |
| `TreeSet<T>` | TreeSet implementation |

### Complex Types

| Type | Generation Strategy |
|------|-------------------|
| Custom Classes | Recursively generated |
| Records | Generated via canonical constructor |
| Nested Objects | Recursively generated |
| Arrays | Not fully supported yet |
| Maps | Limited support (use `Overrider.object()`) |

## Regex Pattern Support

Matriarch's regex generator supports:

### Character Classes

- `[a-z]` - Lowercase letters
- `[A-Z]` - Uppercase letters
- `[0-9]` - Digits
- `[abc]` - Specific characters
- `[a-zA-Z0-9]` - Combined ranges

### Quantifiers

- `{n}` - Exactly n times
- `{n,m}` - Between n and m times
- `*` - Zero or more times
- `+` - One or more times
- `?` - Zero or one time

### Groups and Alternation

- `(option1|option2|option3)` - Alternation
- Nested groups supported

### Escape Sequences

- `\d` - Digit [0-9]
- `\w` - Word character [a-zA-Z0-9_]
- `\s` - Whitespace
- `\.` - Literal dot
- `\\` - Literal backslash

### Examples

```java
"[a-z]{5,10}"                      // 5-10 lowercase letters
"\\d{3}-\\d{3}-\\d{4}"            // Phone: 555-123-4567
"[A-Z]{3}-\\d{6}"                  // ID: ABC-123456
"(gmail|outlook|yahoo)"            // Email provider
"[a-z]+@(gmail|yahoo)\\.com"      // Email pattern
```

## Built-in Suppliers

Matriarch includes 41+ ready-to-use `Supplier` implementations for generating realistic test data.

### Base Classes

#### RandomSupplier&lt;T&gt;

Abstract base class for suppliers that need random number generation.

```java
public abstract class RandomSupplier<T> implements Supplier<T>
```

**Protected Methods:**
- `randomElement(T[] array)` - Select random element from array
- `randomInt(int min, int max)` - Random integer in range [min, max)
- `RANDOM` - Thread-safe Random instance

**Example:**
```java
public class StatusSupplier extends RandomSupplier<String> {
    private static final String[] STATUSES = {"ACTIVE", "INACTIVE", "PENDING"};
    
    @Override
    public String get() {
        return randomElement(STATUSES);
    }
}
```

#### ConfigurableSupplier&lt;T&gt;

Base class for suppliers that need constructor parameters.

```java
public abstract class ConfigurableSupplier<T> extends RandomSupplier<T>
```

**Example:**
```java
public class IntegerRangeSupplier extends ConfigurableSupplier<Integer> {
    private final int min;
    private final int max;
    
    public IntegerRangeSupplier() {
        this(0, 100);
    }
    
    public IntegerRangeSupplier(int min, int max) {
        this.min = min;
        this.max = max;
    }
    
    @Override
    public Integer get() {
        return randomInt(min, max + 1);
    }
}
```

### Personal Suppliers (9 classes)

#### FirstNameSupplier

Generates random first names.

```java
Supplier<String> supplier = new FirstNameSupplier();
String name = supplier.get(); // "John", "Emma", etc.
```

#### LastNameSupplier

Generates random last names.

```java
Supplier<String> supplier = new LastNameSupplier();
String name = supplier.get(); // "Smith", "Johnson", etc.
```

#### FullNameSupplier

Generates full names (first + last).

```java
Supplier<String> supplier = new FullNameSupplier();
String name = supplier.get(); // "John Smith", "Emma Johnson", etc.
```

#### EmailSupplier

Generates realistic email addresses with various patterns.

```java
Supplier<String> supplier = new EmailSupplier();
String email = supplier.get(); // "john.smith@gmail.com", "emma123@yahoo.com", etc.
```

**Usage with Mother:**
```java
User user = Mother.forClass(User.class)
    .forField("email", new EmailSupplier())
    .build();
```

#### PhoneSupplier

Generates US phone numbers in (XXX) XXX-XXXX format.

```java
Supplier<String> supplier = new PhoneSupplier();
String phone = supplier.get(); // "(555) 123-4567"
```

#### UsernameSupplier

Generates creative usernames.

```java
Supplier<String> supplier = new UsernameSupplier();
String username = supplier.get(); // "cool_user123", "john_dev", etc.
```

#### AgeSupplier

Generates ages (default 18-80, configurable).

```java
// Default: 18-80
Supplier<Integer> supplier = new AgeSupplier();
Integer age = supplier.get();

// Custom range
Supplier<Integer> supplier = new AgeSupplier(21, 65);
Integer age = supplier.get();
```

#### DateOfBirthSupplier

Generates date of birth (default 18-80 years old, configurable).

```java
// Default: 18-80 years old
Supplier<LocalDate> supplier = new DateOfBirthSupplier();
LocalDate dob = supplier.get();

// Custom age range
Supplier<LocalDate> supplier = new DateOfBirthSupplier(21, 65);
LocalDate dob = supplier.get();
```

#### GenderSupplier

Generates gender values.

```java
Supplier<String> supplier = new GenderSupplier();
String gender = supplier.get(); // "Male", "Female", "Non-binary", etc.
```

### Company Suppliers (5 classes)

#### CompanyNameSupplier

Generates company names with various patterns.

```java
Supplier<String> supplier = new CompanyNameSupplier();
String name = supplier.get(); // "TechCorp Inc.", "Global Solutions LLC", etc.
```

#### CompanySuffixSupplier

Generates company suffixes.

```java
Supplier<String> supplier = new CompanySuffixSupplier();
String suffix = supplier.get(); // "Inc.", "LLC", "Corp.", "Ltd.", etc.
```

#### JobTitleSupplier

Generates job titles and positions.

```java
Supplier<String> supplier = new JobTitleSupplier();
String title = supplier.get(); // "Senior Software Engineer", "Product Manager", etc.
```

#### DepartmentSupplier

Generates department names.

```java
Supplier<String> supplier = new DepartmentSupplier();
String dept = supplier.get(); // "Engineering", "Marketing", "Sales", etc.
```

#### IndustrySupplier

Generates industry names.

```java
Supplier<String> supplier = new IndustrySupplier();
String industry = supplier.get(); // "Technology", "Healthcare", "Finance", etc.
```

### Address Suppliers (9 classes)

#### CitySupplier

Generates US city names.

```java
Supplier<String> supplier = new CitySupplier();
String city = supplier.get(); // "New York", "Los Angeles", "Chicago", etc.
```

#### StreetAddressSupplier

Generates street addresses.

```java
Supplier<String> supplier = new StreetAddressSupplier();
String address = supplier.get(); // "123 Main St", "456 Oak Avenue", etc.
```

#### StateSupplier

Generates US states, German Bundesländer, and Spanish provinces.

```java
Supplier<String> supplier = new StateSupplier();
String state = supplier.get(); // "California", "Texas", "New York", etc.
```

#### ZipCodeSupplier

Generates ZIP codes (5-digit or ZIP+4 format).

```java
Supplier<String> supplier = new ZipCodeSupplier();
String zip = supplier.get(); // "12345" or "12345-6789"
```

#### CountrySupplier

Generates country names.

```java
Supplier<String> supplier = new CountrySupplier();
String country = supplier.get(); // "United States", "Canada", "Germany", etc.
```

#### CountryCodeSupplier

Generates ISO 3166-1 alpha-2 country codes.

```java
Supplier<String> supplier = new CountryCodeSupplier();
String code = supplier.get(); // "US", "CA", "DE", "FR", etc.
```

#### LatitudeSupplier

Generates latitude coordinates (-90 to 90).

```java
Supplier<Double> supplier = new LatitudeSupplier();
Double lat = supplier.get(); // -45.123456, 37.774929, etc.
```

#### LongitudeSupplier

Generates longitude coordinates (-180 to 180).

```java
Supplier<Double> supplier = new LongitudeSupplier();
Double lon = supplier.get(); // -122.419416, 13.404954, etc.
```

#### CoordinatesSupplier

Generates formatted coordinates (latitude, longitude).

```java
Supplier<String> supplier = new CoordinatesSupplier();
String coords = supplier.get(); // "37.774929, -122.419416"
```

### Internet Suppliers (8 classes)

#### UrlSupplier

Generates HTTP/HTTPS URLs.

```java
Supplier<String> supplier = new UrlSupplier();
String url = supplier.get(); // "https://example.com/path"
```

#### DomainSupplier

Generates domain names with various TLDs.

```java
Supplier<String> supplier = new DomainSupplier();
String domain = supplier.get(); // "example.com", "test.org", etc.
```

#### IpV4AddressSupplier

Generates IPv4 addresses.

```java
Supplier<String> supplier = new IpV4AddressSupplier();
String ip = supplier.get(); // "192.168.1.1"
```

#### IpV6AddressSupplier

Generates IPv6 addresses.

```java
Supplier<String> supplier = new IpV6AddressSupplier();
String ip = supplier.get(); // "2001:0db8:85a3:0000:0000:8a2e:0370:7334"
```

#### MacAddressSupplier

Generates MAC addresses.

```java
Supplier<String> supplier = new MacAddressSupplier();
String mac = supplier.get(); // "01:23:45:67:89:AB"
```

#### PasswordSupplier

Generates secure passwords (configurable length and complexity).

```java
// Default: 12 characters with mixed case, digits, and symbols
Supplier<String> supplier = new PasswordSupplier();
String password = supplier.get();

// Custom length
Supplier<String> supplier = new PasswordSupplier(16);
String password = supplier.get();
```

#### UserAgentSupplier

Generates browser user agent strings.

```java
Supplier<String> supplier = new UserAgentSupplier();
String userAgent = supplier.get(); // "Mozilla/5.0 (Windows NT 10.0; Win64; x64)..."
```

#### SlugSupplier

Generates URL-friendly slugs.

```java
Supplier<String> supplier = new SlugSupplier();
String slug = supplier.get(); // "awesome-product-name", "cool-blog-post", etc.
```

### Text Suppliers (5 classes)

#### LoremIpsumSupplier

Generates Lorem ipsum text (configurable word count).

```java
// Default: 50 words
Supplier<String> supplier = new LoremIpsumSupplier();
String text = supplier.get();

// Custom word count
Supplier<String> supplier = new LoremIpsumSupplier(100);
String text = supplier.get();
```

#### WordSupplier

Generates random words.

```java
Supplier<String> supplier = new WordSupplier();
String word = supplier.get(); // "lorem", "ipsum", "dolor", etc.
```

#### SentenceSupplier

Generates random sentences (configurable word count).

```java
// Default: 5-15 words
Supplier<String> supplier = new SentenceSupplier();
String sentence = supplier.get();

// Custom word count range
Supplier<String> supplier = new SentenceSupplier(10, 20);
String sentence = supplier.get();
```

#### ParagraphSupplier

Generates random paragraphs (configurable sentence count).

```java
// Default: 3-7 sentences
Supplier<String> supplier = new ParagraphSupplier();
String paragraph = supplier.get();

// Custom sentence count
Supplier<String> supplier = new ParagraphSupplier(5, 10);
String paragraph = supplier.get();
```

#### TitleSupplier

Generates book/article titles.

```java
Supplier<String> supplier = new TitleSupplier();
String title = supplier.get(); // "The Great Adventure", "Mystery of the Lost City", etc.
```

### DateTime Suppliers (1 class)

#### PastLocalDateSupplier

Generates past dates (default 365 days, configurable).

```java
// Default: up to 365 days in the past
Supplier<LocalDate> supplier = new PastLocalDateSupplier();
LocalDate date = supplier.get();

// Custom days in past
Supplier<LocalDate> supplier = new PastLocalDateSupplier(90);
LocalDate date = supplier.get();
```

### Numeric Suppliers (1 class)

#### UuidSupplier

Generates random UUIDs.

```java
Supplier<UUID> supplier = new UuidSupplier();
UUID uuid = supplier.get();
```

### Financial Suppliers (5 classes)

#### PriceSupplier

Generates prices as BigDecimal (default 1.00-999.99, configurable).

```java
// Default: 1.00-999.99
Supplier<BigDecimal> supplier = new PriceSupplier();
BigDecimal price = supplier.get();

// Custom range
Supplier<BigDecimal> supplier = new PriceSupplier(
    new BigDecimal("10.00"), 
    new BigDecimal("500.00")
);
BigDecimal price = supplier.get();
```

#### CreditCardSupplier

Generates credit card numbers (test data only).

```java
Supplier<String> supplier = new CreditCardSupplier();
String card = supplier.get(); // "4532-1234-5678-9010"
```

> ⚠️ **Warning:** For test purposes only. Not valid for actual transactions.

#### IbanSupplier

Generates IBAN numbers (test data only).

```java
Supplier<String> supplier = new IbanSupplier();
String iban = supplier.get(); // "DE89370400440532013000"
```

#### CurrencyCodeSupplier

Generates ISO 4217 currency codes.

```java
Supplier<String> supplier = new CurrencyCodeSupplier();
String currency = supplier.get(); // "USD", "EUR", "GBP", etc.
```

#### AccountNumberSupplier

Generates bank account numbers.

```java
Supplier<String> supplier = new AccountNumberSupplier();
String account = supplier.get(); // "1234567890"
```

### Util Suppliers (2 classes)

#### BooleanSupplier

Generates random boolean values.

```java
Supplier<Boolean> supplier = new BooleanSupplier();
Boolean value = supplier.get(); // true or false
```

#### StatusSupplier

Generates status values.

```java
Supplier<String> supplier = new StatusSupplier();
String status = supplier.get(); // "ACTIVE", "INACTIVE", "PENDING", etc.
```

#### PrioritySupplier

Generates priority values.

```java
Supplier<String> supplier = new PrioritySupplier();
String priority = supplier.get(); // "LOW", "MEDIUM", "HIGH", "CRITICAL", "URGENT"
```

### Usage Examples

#### With Builder Pattern

```java
User user = Mother.forClass(User.class)
    .forField("email", new EmailSupplier())
    .forField("fullName", new FullNameSupplier())
    .forField("age", new AgeSupplier(25, 65))
    .forField("companyName", new CompanyNameSupplier())
    .forField("registrationDate", new PastLocalDateSupplier())
    .build();
```

#### With Type-Based Generation

```java
Order order = Mother.forClass(Order.class)
    .forType(String.class, new LoremIpsumSupplier(20))
    .forType(LocalDate.class, new PastLocalDateSupplier(90))
    .forType(BigDecimal.class, new PriceSupplier())
    .build();
```

#### With JUnit Annotations

```java
@ParameterizedTest
@MotherFactoryResource(args = {
    @RandomArg(
        targetClass = User.class,
        overrides = {
            @OverrideField(field = "email", supplier = EmailSupplier.class),
            @OverrideField(field = "name", supplier = FullNameSupplier.class),
            @OverrideField(field = "age", supplier = AgeSupplier.class)
        }
    )
})
void testWithSuppliers(User user) {
    assertNotNull(user.getEmail());
    assertTrue(user.getEmail().contains("@"));
}
```

> **Note:** Supplier-based field overrides in annotations is a planned feature. Currently use builder pattern for supplier integration.

#### Creating Custom Suppliers

```java
// Simple supplier
public class StatusSupplier extends RandomSupplier<String> {
    private static final String[] STATUSES = {"ACTIVE", "INACTIVE", "PENDING"};
    
    @Override
    public String get() {
        return randomElement(STATUSES);
    }
}

// Configurable supplier
public class IntRangeSupplier extends ConfigurableSupplier<Integer> {
    private final int min;
    private final int max;
    
    public IntRangeSupplier() {
        this(0, 100);
    }
    
    public IntRangeSupplier(int min, int max) {
        this.min = min;
        this.max = max;
    }
    
    @Override
    public Integer get() {
        return randomInt(min, max + 1);
    }
}
```

### Supplier Statistics

- **Total Supplier Classes:** 43
- **Base Classes:** 2
- **Domain Suppliers:** 41
  - Personal: 9
  - Company: 5
  - Address: 9
  - Internet: 8
  - Text: 5
  - DateTime: 1
  - Numeric: 1
  - Financial: 5
  - Util: 3

## Error Handling

### Common Exceptions

**ClassNotFoundException**
- Thrown when target class cannot be found
- Solution: Ensure class is in classpath

**IllegalAccessException**
- Thrown when constructor is not accessible
- Solution: Ensure public constructor or factory method exists

**InstantiationException**
- Thrown when object cannot be instantiated
- Solution: Check for abstract classes, interfaces

**StackOverflowError**
- Caused by circular dependencies
- Solution: Use `excludeFields()` to break cycles

**NullPointerException**
- May occur with complex nested structures
- Solution: Use `withCollectionSize()` to ensure non-empty collections

## Version Information

**Current Version:** 1.0.0

**Minimum Java Version:** Java 17

**Dependencies:**
- Jackson (for JSON processing)
- JUnit 5 (for test integration)

## Migration Guide

### From 0.x to 1.0

**Breaking Changes:**
- None - fully backward compatible

**Deprecated:**
- `.override()` - Use `.forField()` instead (override still works)

**New Features:**
- Named test cases
- Direct regex support with `isRegex`
- Enhanced error messages
- JUnit field injection

## Contributing

See the [GitHub repository](https://github.com/giro-dev/matriarch) for:
- Source code
- Issue tracking
- Contribution guidelines
- Release notes

## License

Matriarch is open source software.

## Support

- **Documentation:** [https://giro-dev.github.io/matriarch/](https://giro-dev.github.io/matriarch/)
- **GitHub Issues:** [https://github.com/giro-dev/matriarch/issues](https://github.com/giro-dev/matriarch/issues)
- **Discussions:** [https://github.com/giro-dev/matriarch/discussions](https://github.com/giro-dev/matriarch/discussions)

---

**Last Updated:** 2025-11-15  
**Version:** 1.0.0

