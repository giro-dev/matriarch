---
title: "Advanced Usage"
date: 2025-11-15
draft: false
weight: 7
---

# Advanced Usage

This guide covers advanced scenarios, best practices, and techniques for getting the most out of Matriarch.

## Complex Object Graphs

### Deep Nesting

Matriarch handles deeply nested object structures:

```java
public class Company {
    private Department department;
}

public class Department {
    private Manager manager;
    private List<Employee> employees;
}

public class Manager extends Employee {
    private List<Project> projects;
}

public class Employee {
    private Person personalInfo;
    private Address address;
}

// Generate the entire graph
Company company = Mother.forClass(Company.class)
    .forField("department.manager.personalInfo.name", "John Manager")
    .forField("department.manager.personalInfo.email", "john@company.com")
    .forField("department.employees[0].personalInfo.name", "Employee One")
    .forField("department.employees[1].personalInfo.name", "Employee Two")
    .withCollectionSize(2, 5)
    .build();
```

### Circular Dependencies

Matriarch detects and handles circular references:

```java
public class Parent {
    private List<Child> children;
}

public class Child {
    private Parent parent;  // Circular reference
}

// Matriarch stops recursion to prevent stack overflow
Parent parent = Mother.forClass(Parent.class).build();
// parent.children will be populated, but child.parent will be null
```

**Best Practice:** Design your test objects to avoid circular dependencies when possible, or use `excludeFields()`:

```java
Parent parent = Mother.forClass(Parent.class)
    .excludeFields("children[*].parent")  // Exclude back-references
    .build();
```

## Inheritance and Polymorphism

### Inheritance Hierarchies

```java
public abstract class Animal {
    private String name;
    private int age;
}

public class Dog extends Animal {
    private String breed;
    private boolean trained;
}

public class Cat extends Animal {
    private String color;
    private int lives;
}

// Generate concrete classes
Dog dog = Mother.forClass(Dog.class)
    .forField("name", "Buddy")        // Parent field
    .forField("breed", "Golden Retriever")  // Child field
    .build();

Cat cat = Mother.forClass(Cat.class)
    .forField("name", "Whiskers")
    .forField("lives", 9)
    .build();
```

### Polymorphic Collections

```java
public class Zoo {
    private List<Animal> animals;  // Polymorphic collection
}

// Matriarch uses concrete types when possible
Zoo zoo = Mother.forClass(Zoo.class)
    .forField("animals[0]", new Dog())
    .forField("animals[1]", new Cat())
    .forField("animals[2]", new Dog())
    .build();
```

## Immutable Objects

### Java Records

Matriarch works seamlessly with Java records:

```java
public record User(
    String name,
    String email,
    int age,
    LocalDate createdAt
) {}

User user = Mother.forClass(User.class)
    .forField("name", "John Doe")
    .forField("email", "john@example.com")
    .build();
```

### Immutable Classes with Builders

```java
@Value
@Builder
public class ImmutableOrder {
    String orderId;
    Customer customer;
    List<Item> items;
    Instant createdAt;
}

// Matriarch can work with builder patterns
ImmutableOrder order = Mother.forClass(ImmutableOrder.class)
    .forField("orderId", "ORD-123")
    .forField("customer.email", "customer@example.com")
    .build();
```

### Static Factory Methods

```java
public class Product {
    private final String sku;
    private final String name;
    
    private Product(String sku, String name) {
        this.sku = sku;
        this.name = name;
    }
    
    public static Product create(String sku, String name) {
        return new Product(sku, name);
    }
}

// Matriarch can invoke static factory methods
Product product = Mother.forClass(Product.class).build();
```

## Integration with Testing Libraries

### JavaFaker Integration

```java
import com.github.javafaker.Faker;

public class TestDataFactory {
    private static final Faker faker = new Faker();
    
    public static User createRealisticUser() {
        return Mother.forClass(User.class)
            .forField("firstName", (Supplier<String>) () -> faker.name().firstName())
            .forField("lastName", (Supplier<String>) () -> faker.name().lastName())
            .forField("email", (Supplier<String>) () -> faker.internet().emailAddress())
            .forField("phoneNumber", (Supplier<String>) () -> faker.phoneNumber().phoneNumber())
            .forField("address.street", (Supplier<String>) () -> faker.address().streetAddress())
            .forField("address.city", (Supplier<String>) () -> faker.address().city())
            .forField("address.zipCode", (Supplier<String>) () -> faker.address().zipCode())
            .build();
    }
}
```

### MockMvc Integration

```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testCreateUser() throws Exception {
        User user = Mother.forClass(User.class)
            .forField("email", "test@example.com")
            .build();
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value("test@example.com"));
    }
}
```

### TestContainers Integration

```java
@Testcontainers
class DatabaseIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
    
    @Test
    void testUserPersistence() {
        User user = Mother.forClass(User.class)
            .forField("email", new Regex("[a-z]+@test\\.com"))
            .build();
        
        userRepository.save(user);
        
        User found = userRepository.findByEmail(user.getEmail());
        assertEquals(user.getEmail(), found.getEmail());
    }
}
```

## Performance Optimization

### Stream Generation for Large Datasets

```java
// Generate 10,000 users efficiently
Stream<User> users = Mother.forClass(User.class)
    .buildStream(10_000);

// Process without loading all into memory
long adultCount = users
    .filter(u -> u.getAge() >= 18)
    .count();

// Parallel processing
List<User> processedUsers = Mother.forClass(User.class)
    .buildStream(10_000)
    .parallel()
    .map(this::enrichUser)
    .collect(Collectors.toList());
```

### Reusable Builders

```java
public class TestBuilders {
    
    // Create reusable builder configuration
    private static final Mother<User> BASE_USER_BUILDER = 
        Mother.forClass(User.class)
            .forType(LocalDate.class, () -> LocalDate.now().minusYears(30))
            .forType(Instant.class, Instant::now)
            .withCollectionSize(5, 10);
    
    public static User standardUser() {
        return BASE_USER_BUILDER
            .forField("role", "STANDARD")
            .build();
    }
    
    public static User adminUser() {
        return BASE_USER_BUILDER
            .forField("role", "ADMIN")
            .forField("permissions", "ALL")
            .build();
    }
}
```

> ⚠️ **Note:** Builder instances are not thread-safe. Create separate builders for parallel usage.

### Caching Strategies

```java
public class CachedTestData {
    
    private static final Map<String, User> USER_CACHE = new ConcurrentHashMap<>();
    
    public static User getOrCreateUser(String email) {
        return USER_CACHE.computeIfAbsent(email, e -> 
            Mother.forClass(User.class)
                .forField("email", e)
                .build()
        );
    }
    
    public static void clearCache() {
        USER_CACHE.clear();
    }
}

@BeforeEach
void setUp() {
    CachedTestData.clearCache();
}
```

## Custom Type Handlers

### Custom Generators for Domain Types

```java
public class MoneyAmount {
    private final BigDecimal amount;
    private final String currency;
    
    public MoneyAmount(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }
}

// Create specialized generator
public class TestDataFactories {
    
    private static final Random random = new Random();
    
    public static Supplier<MoneyAmount> moneySupplier() {
        return () -> new MoneyAmount(
            BigDecimal.valueOf(random.nextDouble() * 1000)
                .setScale(2, RoundingMode.HALF_UP),
            "USD"
        );
    }
    
    public static Order createOrder() {
        return Mother.forClass(Order.class)
            .forType(MoneyAmount.class, moneySupplier())
            .build();
    }
}
```

### Enum Strategies

```java
public enum OrderStatus {
    DRAFT, PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
}

// Random enum (default behavior)
Order order1 = Mother.forClass(Order.class).build();
// order1.status could be any value

// Specific enum
Order order2 = Mother.forClass(Order.class)
    .forField("status", "PENDING")
    .build();

// Weighted random selection
Supplier<OrderStatus> weightedStatus = () -> {
    double random = Math.random();
    if (random < 0.5) return OrderStatus.PENDING;
    if (random < 0.8) return OrderStatus.PROCESSING;
    return OrderStatus.SHIPPED;
};

Order order3 = Mother.forClass(Order.class)
    .forField("status", weightedStatus)
    .build();
```

## Test Data Builders

### Fluent Test Data API

```java
public class UserTestBuilder {
    
    private Mother<User> builder = Mother.forClass(User.class);
    
    public UserTestBuilder withEmail(String email) {
        builder = builder.forField("email", email);
        return this;
    }
    
    public UserTestBuilder withRole(String role) {
        builder = builder.forField("role", role);
        return this;
    }
    
    public UserTestBuilder asAdmin() {
        return withRole("ADMIN")
            .withPermissions("ALL");
    }
    
    public UserTestBuilder withPermissions(String permissions) {
        builder = builder.forField("permissions", permissions);
        return this;
    }
    
    public UserTestBuilder inActive() {
        builder = builder.forField("active", false);
        return this;
    }
    
    public User build() {
        return builder.build();
    }
    
    public List<User> buildList(int count) {
        return builder.buildList(count);
    }
}

// Usage
@Test
void testAdminUser() {
    User admin = new UserTestBuilder()
        .asAdmin()
        .withEmail("admin@example.com")
        .build();
    
    assertTrue(admin.isAdmin());
}
```

### Object Mother Pattern

```java
public class UserMother {
    
    public static User defaultUser() {
        return Mother.forClass(User.class).build();
    }
    
    public static User admin() {
        return Mother.forClass(User.class)
            .forField("role", "ADMIN")
            .forField("permissions", "ALL")
            .build();
    }
    
    public static User standardUser() {
        return Mother.forClass(User.class)
            .forField("role", "STANDARD")
            .build();
    }
    
    public static User inactiveUser() {
        return Mother.forClass(User.class)
            .forField("active", false)
            .build();
    }
    
    public static User withEmail(String email) {
        return Mother.forClass(User.class)
            .forField("email", email)
            .build();
    }
    
    public static List<User> multipleUsers(int count) {
        return Mother.forClass(User.class).buildList(count);
    }
}

// Usage in tests
@Test
void testAdminAccess() {
    User admin = UserMother.admin();
    assertTrue(service.hasAccess(admin, "ADMIN_PANEL"));
}

@Test
void testStandardUserAccess() {
    User user = UserMother.standardUser();
    assertFalse(service.hasAccess(user, "ADMIN_PANEL"));
}
```

## Error Handling and Validation

### Handling Generation Failures

```java
@Test
void testWithValidation() {
    try {
        User user = Mother.forClass(User.class)
            .forField("email", "invalid-email")  // May fail validation
            .build();
        
        // Validate after generation
        assertValid(user);
    } catch (Exception e) {
        // Handle generation errors
        fail("Failed to generate user: " + e.getMessage());
    }
}
```

### Post-Generation Validation

```java
public class ValidatingMother {
    
    public static <T> T buildAndValidate(Class<T> clazz, Consumer<Mother<T>> config) {
        Mother<T> builder = Mother.forClass(clazz);
        config.accept(builder);
        
        T instance = builder.build();
        
        // Validate using Bean Validation
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(instance);
        
        if (!violations.isEmpty()) {
            throw new IllegalStateException(
                "Generated object has validation errors: " + violations
            );
        }
        
        return instance;
    }
}

// Usage
@Test
void testValidObject() {
    User user = ValidatingMother.buildAndValidate(User.class, builder -> 
        builder.forField("email", new Regex("[a-z]+@test\\.com"))
    );
    
    assertNotNull(user);
}
```

## Deterministic Generation (Future Feature)

While not yet implemented, here's how deterministic generation might work:

```java
// Future API (not yet available)
User user1 = Mother.forClass(User.class)
    .withSeed(12345L)  // Same seed = same output
    .build();

User user2 = Mother.forClass(User.class)
    .withSeed(12345L)  // Identical to user1
    .build();

assertEquals(user1.getEmail(), user2.getEmail());
```

## Best Practices

### 1. Centralize Test Data Creation

```java
// Good - centralized
public class TestData {
    public static User standardUser() {
        return Mother.forClass(User.class)
            .forField("role", "STANDARD")
            .build();
    }
}

// Bad - scattered throughout tests
@Test
void test1() {
    User user = Mother.forClass(User.class)
        .forField("role", "STANDARD")
        .build();
}
```

### 2. Use Meaningful Names

```java
// Good - clear intent
User adminWithFullAccess = Mother.forClass(User.class)
    .forField("role", "ADMIN")
    .forField("permissions", "ALL")
    .build();

// Bad - unclear
User user1 = Mother.forClass(User.class)
    .forField("role", "ADMIN")
    .build();
```

### 3. Minimize Overrides

```java
// Good - only override what matters
User user = Mother.forClass(User.class)
    .forField("email", "test@example.com")
    .build();

// Bad - over-specification
User user = Mother.forClass(User.class)
    .forField("email", "test@example.com")
    .forField("firstName", "John")
    .forField("lastName", "Doe")
    .forField("age", 30)
    .forField("createdAt", Instant.now())
    // ... many more unnecessary overrides
    .build();
```

### 4. Use Patterns for Common Values

```yaml
# patterns.yaml - better than repeated overrides
patterns:
  - coordinate: email
    value: "[a-z]+@test\\.com"
    type: regex
```

```java
// Now you don't need to override email everywhere
User user = Mother.forClass(User.class).build();
// email automatically matches pattern
```

### 5. Document Complex Generators

```java
/**
 * Creates a user with realistic date distributions:
 * - Created date: within last year
 * - Last login: within last 30 days
 * - Birth date: 18-65 years ago
 */
public static User createRealisticUser() {
    return Mother.forClass(User.class)
        .forType(LocalDate.class, () -> 
            LocalDate.now().minusDays(random.nextInt(365)))
        .forField("lastLogin", (Supplier<Instant>) () ->
            Instant.now().minus(random.nextInt(30), ChronoUnit.DAYS))
        .forField("birthDate", (Supplier<LocalDate>) () ->
            LocalDate.now().minusYears(random.nextInt(18, 66)))
        .build();
}
```

### 6. Separate Test Data from Test Logic

```java
// Good - data separate
@Test
void testUserService() {
    User user = TestData.standardUser();
    
    service.process(user);
    
    assertTrue(user.isProcessed());
}

// Bad - mixed concerns
@Test
void testUserService() {
    User user = Mother.forClass(User.class)
        .forField("role", "STANDARD")
        .forField("email", "test@example.com")
        .forField("active", true)
        .build();
    
    service.process(user);
    
    assertTrue(user.isProcessed());
}
```

## Troubleshooting

### Issue: Null Pointer Exceptions

**Cause:** Nested objects or collections not being populated.

**Solution:**
```java
// Ensure collections have elements
Order order = Mother.forClass(Order.class)
    .withCollectionSize(1, 5)  // Guarantee at least 1 item
    .build();

// Or exclude problematic fields
Order order = Mother.forClass(Order.class)
    .excludeFields("optionalMetadata")
    .build();
```

### Issue: Type Conversion Errors

**Cause:** Incompatible value types.

**Solution:**
```java
// Wrong - string to int fails
.forField("age", "not a number")

// Correct - proper type
.forField("age", 30)
.forField("age", "30")  // Also works - auto-converted
```

### Issue: Circular Reference Stack Overflow

**Cause:** Bidirectional relationships.

**Solution:**
```java
// Exclude back-references
Parent parent = Mother.forClass(Parent.class)
    .excludeFields("children[*].parent")
    .build();
```

### Issue: Slow Test Execution

**Cause:** Generating too much data.

**Solution:**
```java
// Use smaller collections
Mother.forClass(Order.class)
    .withCollectionSize(1, 3)  // Instead of default 1-15
    .build();

// Or use streams for large datasets
Stream<User> users = Mother.forClass(User.class)
    .buildStream(10000);
```

## Next Steps

- **[Examples]({{< ref "/docs/examples.md" >}})** - Real-world usage examples
- **[API Reference]({{< ref "/docs/guide/api-reference.md" >}})** - Complete API documentation
- **[GitHub Repository](https://github.com/giro-dev/matriarch)** - Source code and issues
