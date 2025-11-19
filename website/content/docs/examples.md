---
title: "Examples"
date: 2025-11-15
draft: false
weight: 8
---

# Examples

Real-world examples demonstrating common Matriarch usage patterns.

## E-Commerce Application

### Domain Models

```java
public class Customer {
    private String customerId;
    private String email;
    private String firstName;
    private String lastName;
    private Address billingAddress;
    private Address shippingAddress;
    private List<PaymentMethod> paymentMethods;
    private LocalDate registeredDate;
    private boolean active;
}

public class Order {
    private String orderId;
    private Customer customer;
    private List<OrderItem> items;
    private MoneyAmount total;
    private OrderStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}

public class OrderItem {
    private Product product;
    private int quantity;
    private MoneyAmount price;
}

public class Product {
    private String sku;
    private String name;
    private String description;
    private MoneyAmount price;
    private String category;
    private boolean inStock;
}
```

### Pattern Configuration

```yaml
# src/test/resources/patterns.yaml
patterns:
  - coordinate: email
    value: "[a-z]{5,10}@customer\\.com"
    type: regex

  - coordinate: orderId
    value: "ORD-\\d{8}"
    type: regex

  - coordinate: customerId
    value: "CUST-\\d{8}"
    type: regex

  - coordinate: sku
    value: "[A-Z]{3}-\\d{4}"
    type: regex

  - coordinate: status
    value: "PENDING,PROCESSING,SHIPPED,DELIVERED,CANCELLED"
    type: list

  - coordinate: category
    value: "ELECTRONICS,CLOTHING,BOOKS,HOME,SPORTS"
    type: list
```

### Test Data Factories

```java
public class ECommerceTestData {

    public static Customer standardCustomer() {
        return Mother.forClass(Customer.class)
                .forField("active", true)
                .withCollectionSize(1, 3)
                .build();
    }

    public static Customer vipCustomer() {
        return Mother.forClass(Customer.class)
                .forField("email", new Regex("[a-z]+@vip\\.com"))
                .forField("active", true)
                .withCollectionSize(2, 5)
                .build();
    }

    public static Order pendingOrder() {
        return Mother.forClass(Order.class)
                .forField("status", "PENDING")
                .forType(Instant.class, Instant::now)
                .withCollectionSize(1, 5)
                .build();
    }

    public static Order completedOrder() {
        return Mother.forClass(Order.class)
                .forField("status", "DELIVERED")
                .forField("createdAt", (Supplier<Instant>) () ->
                        Instant.now().minus(7, ChronoUnit.DAYS))
                .forField("updatedAt", Instant.now())
                .withCollectionSize(1, 3)
                .build();
    }

    public static Product electronicProduct() {
        return Mother.forClass(Product.class)
                .forField("category", "ELECTRONICS")
                .forField("inStock", true)
                .build();
    }
}
```

### Service Tests

```java
@MotherConfig(collectionMin = 1, collectionMax = 5)
class OrderServiceTest {
    
    @Autowired
    private OrderService orderService;
    
    @Mother(overrides = {
        @OverrideField(field = "status", value = "PENDING"),
        @OverrideField(field = "customer.active", value = "true")
    })
    Order pendingOrder;
    
    @Test
    void shouldProcessPendingOrder() {
        orderService.processOrder(pendingOrder);
        
        assertEquals("PROCESSING", pendingOrder.getStatus());
        assertNotNull(pendingOrder.getUpdatedAt());
    }
    
    @Test
    void shouldCalculateOrderTotal() {
        Order order = ECommerceTestData.pendingOrder();
        
        MoneyAmount total = orderService.calculateTotal(order);
        
        assertNotNull(total);
        assertTrue(total.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }
    
    @ParameterizedTest(name = "Order status: {0}")
    @MotherFactoryResource(args = {
        @RandomArg(name = "PENDING", targetClass = Order.class,
                   overrides = @OverrideField(field = "status", value = "PENDING")),
        @RandomArg(name = "PROCESSING", targetClass = Order.class,
                   overrides = @OverrideField(field = "status", value = "PROCESSING")),
        @RandomArg(name = "SHIPPED", targetClass = Order.class,
                   overrides = @OverrideField(field = "status", value = "SHIPPED"))
    })
    void shouldHandleDifferentOrderStatuses(Order order) {
        boolean canCancel = orderService.canCancel(order);
        
        if (order.getStatus().equals("SHIPPED")) {
            assertFalse(canCancel);
        } else {
            assertTrue(canCancel);
        }
    }
}
```

## Banking Application

### Domain Models

```java
public class Account {
    private String accountNumber;
    private Customer owner;
    private AccountType type;
    private MoneyAmount balance;
    private List<Transaction> transactions;
    private LocalDate openedDate;
    private boolean active;
}

public class Transaction {
    private String transactionId;
    private TransactionType type;
    private MoneyAmount amount;
    private Account fromAccount;
    private Account toAccount;
    private Instant timestamp;
    private String description;
}

public enum TransactionType {
    DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT
}

public enum AccountType {
    CHECKING, SAVINGS, CREDIT, INVESTMENT
}
```

### Pattern Configuration

```yaml
# patterns.yaml
patterns:
  - coordinate: accountNumber
    value: "\\d{10}"
    type: regex

  - coordinate: transactionId
    value: "TXN-\\d{12}"
    type: regex

  - coordinate: type
    value: "CHECKING,SAVINGS,INVESTMENT"
    type: list
```

### Test Data Factories

```java
public class BankingTestData {

    private static final Random random = new Random();

    public static Account checkingAccountWithBalance(BigDecimal balance) {
        return Mother.forClass(Account.class)
                .forField("type", "CHECKING")
                .forField("balance.amount", balance)
                .forField("balance.currency", "USD")
                .forField("active", true)
                .excludeFields("transactions")  // Avoid generating transactions
                .build();
    }

    public static Transaction depositTransaction(BigDecimal amount) {
        return Mother.forClass(Transaction.class)
                .forField("type", "DEPOSIT")
                .forField("amount.amount", amount)
                .forField("amount.currency", "USD")
                .forField("timestamp", Instant.now())
                .excludeFields("fromAccount")
                .build();
    }

    public static Transaction transferTransaction(
            Account from, Account to, BigDecimal amount) {
        return Mother.forClass(Transaction.class)
                .forField("type", "TRANSFER")
                .forField("amount.amount", amount)
                .forField("amount.currency", "USD")
                .forField("fromAccount", from)
                .forField("toAccount", to)
                .forField("timestamp", Instant.now())
                .build();
    }
}
```

### Service Tests

```java
class AccountServiceTest {

    @Test
    void shouldTransferFundsBetweenAccounts() {
        Account from = BankingTestData.checkingAccountWithBalance(
                new BigDecimal("1000.00"));
        Account to = BankingTestData.checkingAccountWithBalance(
                new BigDecimal("500.00"));

        BigDecimal transferAmount = new BigDecimal("200.00");

        accountService.transfer(from, to, transferAmount);

        assertEquals(new BigDecimal("800.00"), from.getBalance().getAmount());
        assertEquals(new BigDecimal("700.00"), to.getBalance().getAmount());
    }

    @ParameterizedTest
    @MotherFactoryResource(args = {
            @RandomArg(name = "Deposit", targetClass = Transaction.class,
                    overrides = @OverrideField(field = "type", value = "DEPOSIT")),
            @RandomArg(name = "Withdrawal", targetClass = Transaction.class,
                    overrides = @OverrideField(field = "type", value = "WITHDRAWAL"))
    })
    void shouldProcessDifferentTransactionTypes(Transaction transaction) {
        boolean success = transactionService.process(transaction);

        assertTrue(success);
        assertNotNull(transaction.getTimestamp());
    }
}
```

## User Management System

### Domain Models

```java
public class User {
    private String userId;
    private String username;
    private String email;
    private String password;
    private UserRole role;
    private Profile profile;
    private List<Permission> permissions;
    private LocalDateTime lastLogin;
    private boolean active;
}

public class Profile {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthDate;
    private Address address;
    private Map<String, String> preferences;
}

public enum UserRole {
    ADMIN, MANAGER, USER, GUEST
}
```

### Test Data Factories

```java
public class UserTestData {

    public static User adminUser() {
        return Mother.forClass(User.class)
                .forField("role", "ADMIN")
                .forField("active", true)
                .forType(LocalDateTime.class, LocalDateTime::now)
                .withCollectionSize(5, 10)  // More permissions for admin
                .build();
    }

    public static User standardUser() {
        return Mother.forClass(User.class)
                .forField("role", "USER")
                .forField("active", true)
                .withCollectionSize(1, 3)
                .build();
    }

    public static User inactiveUser() {
        return Mother.forClass(User.class)
                .forField("active", false)
                .forField("lastLogin", (Supplier<LocalDateTime>) () ->
                        LocalDateTime.now().minusDays(90))
                .build();
    }

    public static User userWithProfile(String email, String firstName, String lastName) {
        return Mother.forClass(User.class)
                .forField("email", email)
                .forField("profile.firstName", firstName)
                .forField("profile.lastName", lastName)
                .forField("active", true)
                .build();
    }
}
```

### Controller Tests

```java

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void shouldCreateNewUser() throws Exception {
        User newUser = Mother.forClass(User.class)
                .forField("email", new Regex("[a-z]{5}@test\\.com"))
                .forField("role", "USER")
                .excludeFields("userId", "lastLogin")
                .build();

        when(userService.createUser(any())).thenReturn(newUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @ParameterizedTest(name = "Test role: {0}")
    @MotherFactoryResource(args = {
            @RandomArg(name = "ADMIN", targetClass = User.class,
                    overrides = @OverrideField(field = "role", value = "ADMIN")),
            @RandomArg(name = "MANAGER", targetClass = User.class,
                    overrides = @OverrideField(field = "role", value = "MANAGER")),
            @RandomArg(name = "USER", targetClass = User.class,
                    overrides = @OverrideField(field = "role", value = "USER"))
    })
    void shouldHandleDifferentUserRoles(User user) throws Exception {
        when(userService.findById(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/" + user.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value(user.getRole().toString()));
    }
}
```

## Microservices Integration

### Event-Driven Architecture

```java
public class OrderCreatedEvent {
    private String eventId;
    private String orderId;
    private String customerId;
    private MoneyAmount orderTotal;
    private Instant timestamp;
    private Map<String, Object> metadata;
}

public class OrderTestData {

    public static OrderCreatedEvent orderCreatedEvent() {
        return Mother.forClass(OrderCreatedEvent.class)
                .forField("eventId", (Supplier<String>) () ->
                        "EVT-" + UUID.randomUUID())
                .forField("orderId", new Regex("ORD-\\d{8}"))
                .forField("timestamp", Instant.now())
                .build();
    }
}
```

### Message Queue Tests

```java

@SpringBootTest
class OrderEventHandlerTest {

    @Autowired
    private OrderEventHandler eventHandler;

    @Test
    void shouldProcessOrderCreatedEvent() {
        OrderCreatedEvent event = OrderTestData.orderCreatedEvent();

        eventHandler.handle(event);

        // Verify event processing
        assertTrue(eventHandler.wasProcessed(event.getEventId()));
    }

    @Test
    void shouldHandleMultipleEvents() {
        List<OrderCreatedEvent> events = Mother.forClass(OrderCreatedEvent.class)
                .forType(Instant.class, Instant::now)
                .buildList(10);

        events.forEach(eventHandler::handle);

        assertEquals(10, eventHandler.getProcessedCount());
    }
}
```

## REST API Testing

### API Request/Response Models

```java
public class CreateUserRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private UserRole role;
}

public class CreateUserResponse {
    private String userId;
    private String email;
    private LocalDateTime createdAt;
    private String status;
}
```

### Integration Tests

```java

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserApiIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateUserViaApi() {
        CreateUserRequest request = Mother.forClass(CreateUserRequest.class)
                .forField("email", new Regex("[a-z]{5,10}@test\\.com"))
                .forField("role", "USER")
                .build();

        ResponseEntity<CreateUserResponse> response = restTemplate
                .postForEntity("/api/users", request, CreateUserResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(request.getEmail(), response.getBody().getEmail());
    }

    @Test
    void shouldValidateEmailFormat() {
        CreateUserRequest invalidRequest = Mother.forClass(CreateUserRequest.class)
                .forField("email", "invalid-email")
                .build();

        ResponseEntity<String> response = restTemplate
                .postForEntity("/api/users", invalidRequest, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
```

## Data-Driven Testing

### CSV-style Test Cases

```java
class UserValidationTest {

    @ParameterizedTest(name = "[{index}] {0}")
    @MotherFactoryResource(args = {
            @RandomArg(
                    name = "Valid Gmail",
                    targetClass = User.class,
                    overrides = @OverrideField(
                            field = "email",
                            value = "[a-z]{5,10}@gmail\\.com",
                            isRegex = true
                    )
            ),
            @RandomArg(
                    name = "Valid Outlook",
                    targetClass = User.class,
                    overrides = @OverrideField(
                            field = "email",
                            value = "[a-z]{5,10}@outlook\\.com",
                            isRegex = true
                    )
            ),
            @RandomArg(
                    name = "Valid Yahoo",
                    targetClass = User.class,
                    overrides = @OverrideField(
                            field = "email",
                            value = "[a-z]{5,10}@yahoo\\.com",
                            isRegex = true
                    )
            )
    })
    void shouldValidateDifferentEmailDomains(User user) {
        assertTrue(validator.isValidEmail(user.getEmail()));
    }
}
```

### Complex JSON Test Cases

```java

@ParameterizedTest
@MotherFactoryResource(args = {
        @RandomArg(
                name = "Premium Order",
                targetClass = Order.class,
                jsonOverrides = """
                        {
                            "orderId": "ORD-PREMIUM-001",
                            "customer": {
                                "tier": "PREMIUM",
                                "email": "premium@example.com"
                            },
                            "items": [
                                {"sku": "PREM-001", "quantity": 1},
                                {"sku": "PREM-002", "quantity": 2}
                            ],
                            "shippingMethod": "EXPRESS",
                            "discountApplied": true
                        }
                        """
        ),
        @RandomArg(
                name = "Standard Order",
                targetClass = Order.class,
                jsonOverrides = """
                        {
                            "orderId": "ORD-STANDARD-001",
                            "customer": {
                                "tier": "STANDARD",
                                "email": "standard@example.com"
                            },
                            "items": [
                                {"sku": "STD-001", "quantity": 1}
                            ],
                            "shippingMethod": "STANDARD",
                            "discountApplied": false
                        }
                        """
        )
})
void shouldCalculateShippingByTier(Order order) {
    BigDecimal shipping = shippingService.calculateShipping(order);

    if (order.getCustomer().getTier().equals("PREMIUM")) {
        assertEquals(BigDecimal.ZERO, shipping);  // Free shipping
    } else {
        assertTrue(shipping.compareTo(BigDecimal.ZERO) > 0);
    }
}
```

## Performance Testing

### Load Test Data Generation

```java
class PerformanceTest {

    @Test
    void shouldHandleLargeDataset() {
        // Generate 100,000 users efficiently
        Stream<User> users = Mother.forClass(User.class)
                .forType(LocalDateTime.class, LocalDateTime::now)
                .buildStream(100_000);

        long startTime = System.currentTimeMillis();

        long activeUsers = users
                .parallel()
                .filter(User::isActive)
                .count();

        long duration = System.currentTimeMillis() - startTime;

        assertTrue(activeUsers > 0);
        assertTrue(duration < 10000);  // Should complete in under 10 seconds
    }

    @Test
    void shouldBatchProcessOrders() {
        List<Order> orders = Mother.forClass(Order.class)
                .forField("status", "PENDING")
                .withCollectionSize(1, 3)
                .buildList(1000);

        List<List<Order>> batches = Lists.partition(orders, 100);

        batches.parallelStream()
                .forEach(batch -> orderService.processBatch(batch));

        assertEquals(1000, orderService.getProcessedCount());
    }
}
```

## Best Practices Summary

### 1. Create Domain-Specific Factories

```java
// Good - domain-specific
public class ECommerceTestData {
    public static Customer vipCustomer() { ...}

    public static Order largeOrder() { ...}
}

// Bad - generic everywhere
public class TestData {
    public static <T> T create(Class<T> clazz) { ...}
}
```

### 2. Use Patterns for Common Fields

```yaml
# patterns.yaml
patterns:
  - coordinate: email
    value: "[a-z]+@test\\.com"
    type: regex
```

### 3. Leverage Named Test Cases

```java
@ParameterizedTest(name = "[{index}] {0}")
@MotherFactoryResource(args = {
        @RandomArg(name = "Admin User",...),
@RandomArg(name = "Standard User", ...)
        })
```

### 4. Combine with Other Testing Tools

```java
// With Mockito
when(service.find(any())).

thenReturn(
        Mother.forClass(User.class).

build()
);

// With AssertJ
assertThat(Mother.forClass(User.class).

buildList(10))
        .

hasSize(10)
    .

allMatch(User::isActive);
```

## Next Steps

- **[API Reference]({{< ref "api-reference.md" >}})** - Complete API documentation
- **[Advanced Usage]({{< ref "advanced-usage.md" >}})** - Complex scenarios
- **[GitHub Repository](https://github.com/giro-dev/matriarch)** - More examples and source code

