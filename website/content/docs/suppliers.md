---
title: "Built-in Suppliers"
date: 2025-11-15
draft: false
weight: 10
---

# Built-in Suppliers

Matriarch includes **43 ready-to-use Supplier implementations** for generating realistic test data. These suppliers can
be used with the builder API or JUnit annotations to create domain-specific test objects with minimal code.

## Quick Start

```java
// Use suppliers with the builder API
User user = Mother.forClass(User.class)
                .forField("email", new EmailSupplier())
                .forField("fullName", new FullNameSupplier())
                .forField("age", new AgeSupplier(25, 65))
                .forField("company", new CompanyNameSupplier())
                .build();

// Direct usage
EmailSupplier emailSupplier = new EmailSupplier();
String email = emailSupplier.get(); // "john.smith@gmail.com"
```

## Why Use Suppliers?

✅ **Realistic Data** - Generate data that looks like real-world values  
✅ **Type-Safe** - Full compile-time type checking  
✅ **Reusable** - Share supplier instances across tests  
✅ **Configurable** - Many suppliers accept parameters for customization  
✅ **Composable** - Combine suppliers for complex scenarios  
✅ **No Dependencies** - Lightweight with no external data libraries

## Supplier Categories

### 🧑 Personal (9 suppliers)

Generate personal information like names, emails, ages, and contact details.

### 🏢 Company (5 suppliers)

Business and corporate data including company names, job titles, and departments.

### 🏠 Address (9 suppliers)

Location data from street addresses to geographic coordinates.

### 🌐 Internet (8 suppliers)

Web-related data like URLs, IP addresses, and user agents.

### 📝 Text (5 suppliers)

Content generation from single words to full paragraphs.

### 📅 DateTime (1 supplier)

Date and time generation for various scenarios.

### 🔢 Numeric (1 supplier)

Identifiers and unique values.

### 💰 Financial (5 suppliers)

Banking and financial test data.

### 🔧 Util (3 suppliers)

Utility values like booleans, status, and priority.

---

## Personal Suppliers

### FirstNameSupplier

Generates random first names from a diverse set.

```java
FirstNameSupplier supplier = new FirstNameSupplier();
String name = supplier.get(); // "Emma", "Liam", "Olivia", "Noah"
```

**Use cases:** User profiles, contact lists, customer data

---

### LastNameSupplier

Generates random last names.

```java
LastNameSupplier supplier = new LastNameSupplier();
String name = supplier.get(); // "Smith", "Johnson", "Williams"
```

---

### FullNameSupplier

Combines first and last names.

```java
FullNameSupplier supplier = new FullNameSupplier();
String name = supplier.get(); // "Emma Johnson", "Liam Smith"
```

**Example:**

```java
List<String> names = Mother.forClass(String.class)
        .forType(String.class, new FullNameSupplier())
        .buildList(10);
```

---

### EmailSupplier

Generates realistic email addresses with various patterns.

```java
EmailSupplier supplier = new EmailSupplier();
String email = supplier.get();
// Possible outputs:
// - "john.smith@gmail.com"
// - "emmasmith@yahoo.com"
// - "liam_johnson@outlook.com"
// - "olivia123@hotmail.com"
```

**Domains used:** gmail.com, yahoo.com, outlook.com, hotmail.com, icloud.com, protonmail.com, mail.com, aol.com,
zoho.com, fastmail.com

**Example:**

```java
User user = Mother.forClass(User.class)
        .forField("email", new EmailSupplier())
        .forField("alternateEmail", new EmailSupplier())
        .build();
```

---

### PhoneSupplier

Generates US phone numbers in standard format.

```java
PhoneSupplier supplier = new PhoneSupplier();
String phone = supplier.get(); // "(555) 123-4567"
```

**Format:** `(XXX) XXX-XXXX`

---

### UsernameSupplier

Creates creative usernames.

```java
UsernameSupplier supplier = new UsernameSupplier();
String username = supplier.get();
// Examples: "cool_user123", "john_dev", "emma_tech99"
```

---

### AgeSupplier

Generates ages with configurable range.

```java
// Default: 18-80
AgeSupplier supplier = new AgeSupplier();
Integer age = supplier.get();

// Custom range: 21-65
AgeSupplier supplier = new AgeSupplier(21, 65);
Integer age = supplier.get();
```

**Example:**

```java
// Generate adult users
User user = Mother.forClass(User.class)
                .forField("age", new AgeSupplier(18, 100))
                .build();

// Generate working-age population
User employee = Mother.forClass(User.class)
        .forField("age", new AgeSupplier(22, 67))
        .build();
```

---

### DateOfBirthSupplier

Generates birth dates based on age range.

```java
// Default: 18-80 years old
DateOfBirthSupplier supplier = new DateOfBirthSupplier();
LocalDate dob = supplier.get();

// Custom age range: 21-65
DateOfBirthSupplier supplier = new DateOfBirthSupplier(21, 65);
LocalDate dob = supplier.get();
```

---

### GenderSupplier

Generates gender values.

```java
GenderSupplier supplier = new GenderSupplier();
String gender = supplier.get(); // "Male", "Female", "Non-binary", etc.
```

---

## Company Suppliers

### CompanyNameSupplier

Generates company names with various patterns.

```java
CompanyNameSupplier supplier = new CompanyNameSupplier();
String name = supplier.get();
// Examples: "TechCorp Inc.", "Global Solutions LLC", "Innovate Systems Corp."
```

---

### CompanySuffixSupplier

Company legal suffixes.

```java
CompanySuffixSupplier supplier = new CompanySuffixSupplier();
String suffix = supplier.get(); // "Inc.", "LLC", "Corp.", "Ltd.", "PLC"
```

---

### JobTitleSupplier

Professional job titles.

```java
JobTitleSupplier supplier = new JobTitleSupplier();
String title = supplier.get();
// Examples: "Senior Software Engineer", "Product Manager", "DevOps Specialist"
```

---

### DepartmentSupplier

Company department names.

```java
DepartmentSupplier supplier = new DepartmentSupplier();
String dept = supplier.get(); // "Engineering", "Marketing", "Sales", "HR"
```

---

### IndustrySupplier

Industry classifications.

```java
IndustrySupplier supplier = new IndustrySupplier();
String industry = supplier.get();
// Examples: "Technology", "Healthcare", "Finance", "Retail"
```

**Example - Create employee profile:**

```java
Employee employee = Mother.forClass(Employee.class)
        .forField("fullName", new FullNameSupplier())
        .forField("email", new EmailSupplier())
        .forField("jobTitle", new JobTitleSupplier())
        .forField("department", new DepartmentSupplier())
        .forField("company.name", new CompanyNameSupplier())
        .forField("company.industry", new IndustrySupplier())
        .build();
```

---

## Address Suppliers

### CitySupplier

US city names.

```java
CitySupplier supplier = new CitySupplier();
String city = supplier.get();
// Examples: "New York", "Los Angeles", "Chicago", "Houston"
```

---

### StreetAddressSupplier

Street addresses.

```java
StreetAddressSupplier supplier = new StreetAddressSupplier();
String address = supplier.get();
// Examples: "123 Main St", "456 Oak Avenue", "789 Elm Boulevard"
```

---

### StateSupplier

US states, German Bundesländer, and Spanish provinces.

```java
StateSupplier supplier = new StateSupplier();
String state = supplier.get();
// Examples: "California", "Texas", "New York", "Florida"
```

---

### ZipCodeSupplier

ZIP codes in US format.

```java
ZipCodeSupplier supplier = new ZipCodeSupplier();
String zip = supplier.get(); // "12345" or "12345-6789"
```

---

### CountrySupplier

Country names.

```java
CountrySupplier supplier = new CountrySupplier();
String country = supplier.get();
// Examples: "United States", "Canada", "Germany", "Japan"
```

---

### CountryCodeSupplier

ISO 3166-1 alpha-2 country codes.

```java
CountryCodeSupplier supplier = new CountryCodeSupplier();
String code = supplier.get(); // "US", "CA", "DE", "FR", "JP"
```

---

### LatitudeSupplier

Latitude coordinates.

```java
LatitudeSupplier supplier = new LatitudeSupplier();
Double lat = supplier.get(); // Range: -90.0 to 90.0
```

---

### LongitudeSupplier

Longitude coordinates.

```java
LongitudeSupplier supplier = new LongitudeSupplier();
Double lon = supplier.get(); // Range: -180.0 to 180.0
```

---

### CoordinatesSupplier

Formatted coordinate pairs.

```java
CoordinatesSupplier supplier = new CoordinatesSupplier();
String coords = supplier.get(); // "37.774929, -122.419416"
```

**Example - Complete address:**

```java
Address address = Mother.forClass(Address.class)
        .forField("street", new StreetAddressSupplier())
        .forField("city", new CitySupplier())
        .forField("state", new StateSupplier())
        .forField("zipCode", new ZipCodeSupplier())
        .forField("country", new CountrySupplier())
        .forField("coordinates", new CoordinatesSupplier())
        .build();
```

---

## Internet Suppliers

### UrlSupplier

HTTP/HTTPS URLs.

```java
UrlSupplier supplier = new UrlSupplier();
String url = supplier.get();
// Examples: "https://example.com", "http://test.org/path"
```

---

### DomainSupplier

Domain names with various TLDs.

```java
DomainSupplier supplier = new DomainSupplier();
String domain = supplier.get();
// Examples: "example.com", "test.org", "mysite.net"
```

---

### IpV4AddressSupplier

IPv4 addresses.

```java
IpV4AddressSupplier supplier = new IpV4AddressSupplier();
String ip = supplier.get(); // "192.168.1.1", "10.0.0.50"
```

---

### IpV6AddressSupplier

IPv6 addresses.

```java
IpV6AddressSupplier supplier = new IpV6AddressSupplier();
String ip = supplier.get();
// "2001:0db8:85a3:0000:0000:8a2e:0370:7334"
```

---

### MacAddressSupplier

MAC addresses.

```java
MacAddressSupplier supplier = new MacAddressSupplier();
String mac = supplier.get(); // "01:23:45:67:89:AB"
```

---

### PasswordSupplier

Secure passwords with configurable length.

```java
// Default: 12 characters
PasswordSupplier supplier = new PasswordSupplier();
String password = supplier.get(); // "aB3$xY9#mK2!"

// Custom length: 16 characters
PasswordSupplier supplier = new PasswordSupplier(16);
String password = supplier.get();
```

**Includes:** Uppercase, lowercase, digits, and special characters

---

### UserAgentSupplier

Browser user agent strings.

```java
UserAgentSupplier supplier = new UserAgentSupplier();
String userAgent = supplier.get();
// "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36..."
```

---

### SlugSupplier

URL-friendly slugs.

```java
SlugSupplier supplier = new SlugSupplier();
String slug = supplier.get();
// Examples: "awesome-product-name", "cool-blog-post-123"
```

---

## Text Suppliers

### LoremIpsumSupplier

Lorem ipsum text with configurable word count.

```java
// Default: 50 words
LoremIpsumSupplier supplier = new LoremIpsumSupplier();
String text = supplier.get();

// Custom: 100 words
LoremIpsumSupplier supplier = new LoremIpsumSupplier(100);
String text = supplier.get();
```

---

### WordSupplier

Single random words.

```java
WordSupplier supplier = new WordSupplier();
String word = supplier.get(); // "lorem", "ipsum", "dolor"
```

---

### SentenceSupplier

Random sentences.

```java
// Default: 5-15 words
SentenceSupplier supplier = new SentenceSupplier();
String sentence = supplier.get();

// Custom word count: 10-20 words
SentenceSupplier supplier = new SentenceSupplier(10, 20);
String sentence = supplier.get();
```

---

### ParagraphSupplier

Multi-sentence paragraphs.

```java
// Default: 3-7 sentences
ParagraphSupplier supplier = new ParagraphSupplier();
String paragraph = supplier.get();

// Custom: 5-10 sentences
ParagraphSupplier supplier = new ParagraphSupplier(5, 10);
String paragraph = supplier.get();
```

---

### TitleSupplier

Book/article titles.

```java
TitleSupplier supplier = new TitleSupplier();
String title = supplier.get();
// Examples: "The Great Adventure", "Mystery of the Lost City"
```

**Example - Blog post:**

```java
BlogPost post = Mother.forClass(BlogPost.class)
        .forField("title", new TitleSupplier())
        .forField("author", new FullNameSupplier())
        .forField("content", new ParagraphSupplier(10, 15))
        .forField("slug", new SlugSupplier())
        .build();
```

---

## DateTime Suppliers

### PastLocalDateSupplier

Past dates with configurable range.

```java
// Default: up to 365 days in the past
PastLocalDateSupplier supplier = new PastLocalDateSupplier();
LocalDate date = supplier.get();

// Custom: up to 90 days in the past
PastLocalDateSupplier supplier = new PastLocalDateSupplier(90);
LocalDate date = supplier.get();
```

**Example:**

```java
Order order = Mother.forClass(Order.class)
        .forField("createdDate", new PastLocalDateSupplier(30))
        .forField("lastUpdated", new PastLocalDateSupplier(7))
        .build();
```

---

## Numeric Suppliers

### UuidSupplier

Random UUIDs.

```java
UuidSupplier supplier = new UuidSupplier();
UUID uuid = supplier.get(); // "550e8400-e29b-41d4-a716-446655440000"
```

---

## Financial Suppliers

### PriceSupplier

Monetary amounts as BigDecimal.

```java
// Default: 1.00-999.99
PriceSupplier supplier = new PriceSupplier();
BigDecimal price = supplier.get();

// Custom range: 10.00-500.00
PriceSupplier supplier = new PriceSupplier(
        new BigDecimal("10.00"),
        new BigDecimal("500.00")
);
BigDecimal price = supplier.get();
```

---

### CreditCardSupplier

Test credit card numbers.

```java
CreditCardSupplier supplier = new CreditCardSupplier();
String card = supplier.get(); // "4532-1234-5678-9010"
```

> ⚠️ **Test data only** - Not valid for actual transactions

---

### IbanSupplier

International bank account numbers.

```java
IbanSupplier supplier = new IbanSupplier();
String iban = supplier.get(); // "DE89370400440532013000"
```

---

### CurrencyCodeSupplier

ISO 4217 currency codes.

```java
CurrencyCodeSupplier supplier = new CurrencyCodeSupplier();
String currency = supplier.get(); // "USD", "EUR", "GBP", "JPY"
```

---

### AccountNumberSupplier

Bank account numbers.

```java
AccountNumberSupplier supplier = new AccountNumberSupplier();
String account = supplier.get(); // "1234567890"
```

**Example - Financial transaction:**

```java
Transaction transaction = Mother.forClass(Transaction.class)
        .forField("amount", new PriceSupplier())
        .forField("currency", new CurrencyCodeSupplier())
        .forField("accountNumber", new AccountNumberSupplier())
        .forField("transactionId", new UuidSupplier())
        .build();
```

---

## Util Suppliers

### BooleanSupplier

Random boolean values.

```java
BooleanSupplier supplier = new BooleanSupplier();
Boolean value = supplier.get(); // true or false
```

---

### StatusSupplier

Common status values.

```java
StatusSupplier supplier = new StatusSupplier();
String status = supplier.get();
// "ACTIVE", "INACTIVE", "PENDING", "SUSPENDED", etc.
```

---

### PrioritySupplier

Priority levels.

```java
PrioritySupplier supplier = new PrioritySupplier();
String priority = supplier.get();
// "LOW", "MEDIUM", "HIGH", "CRITICAL", "URGENT"
```

---

## Creating Custom Suppliers

### Simple Supplier

Extend `RandomSupplier<T>` for basic suppliers:

```java
public class ColorSupplier extends RandomSupplier<String> {
    private static final String[] COLORS = {
            "Red", "Blue", "Green", "Yellow", "Purple", "Orange"
    };

    @Override
    public String get() {
        return randomElement(COLORS);
    }
}

// Usage
ColorSupplier supplier = new ColorSupplier();
String color = supplier.get();
```

### Configurable Supplier

Extend `ConfigurableSupplier<T>` when parameters are needed:

```java
public class IntegerRangeSupplier extends ConfigurableSupplier<Integer> {
    private final int min;
    private final int max;

    // Always provide a no-arg constructor
    public IntegerRangeSupplier() {
        this(0, 100);
    }

    public IntegerRangeSupplier(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("min must be less than max");
        }
        this.min = min;
        this.max = max;
    }

    @Override
    public Integer get() {
        return randomInt(min, max + 1);
    }
}

// Usage
IntegerRangeSupplier supplier = new IntegerRangeSupplier(1, 100);
Integer number = supplier.get();
```

### Best Practices

1. **Stateless** - Keep suppliers stateless when possible
2. **No-arg Constructor** - Always provide for annotation compatibility
3. **Validate Parameters** - Check constructor arguments
4. **Document Behavior** - Clear Javadoc for defaults and options
5. **Thread-Safe** - Use the provided `RANDOM` instance
6. **Realistic Data** - Generate values that resemble real-world data

## Advanced Usage

### Combining Multiple Suppliers

```java
Customer customer = Mother.forClass(Customer.class)
        // Personal info
        .forField("firstName", new FirstNameSupplier())
        .forField("lastName", new LastNameSupplier())
        .forField("email", new EmailSupplier())
        .forField("phone", new PhoneSupplier())
        .forField("dateOfBirth", new DateOfBirthSupplier(25, 65))

        // Address
        .forField("address.street", new StreetAddressSupplier())
        .forField("address.city", new CitySupplier())
        .forField("address.state", new StateSupplier())
        .forField("address.zipCode", new ZipCodeSupplier())
        .forField("address.country", new CountrySupplier())

        // Account info
        .forField("accountNumber", new AccountNumberSupplier())
        .forField("status", new StatusSupplier())
        .forField("priority", new PrioritySupplier())

        .build();
```

### Type-Based Supplier Application

```java
// Apply EmailSupplier to all String fields
User user = Mother.forClass(User.class)
                .forType(String.class, new EmailSupplier())
                .forType(LocalDate.class, new PastLocalDateSupplier(30))
                .forType(BigDecimal.class, new PriceSupplier())
                .build();
```

### Reusing Supplier Instances

```java
// Create supplier instances
EmailSupplier emailSupplier = new EmailSupplier();
FullNameSupplier nameSupplier = new FullNameSupplier();
CompanyNameSupplier companySupplier = new CompanyNameSupplier();

// Use across multiple objects
List<User> users = IntStream.range(0, 100)
        .mapToObj(i -> Mother.forClass(User.class)
                .forField("email", emailSupplier)
                .forField("fullName", nameSupplier)
                .forField("company", companySupplier)
                .build())
        .collect(Collectors.toList());
```

## Summary

Matriarch's **43 built-in suppliers** cover most common test data needs:

| Category  | Count  | Purpose                      |
|-----------|--------|------------------------------|
| Personal  | 9      | Names, emails, contact info  |
| Company   | 5      | Business and corporate data  |
| Address   | 9      | Location and geographic data |
| Internet  | 8      | Web and network related      |
| Text      | 5      | Content generation           |
| DateTime  | 1      | Date and time values         |
| Numeric   | 1      | Identifiers                  |
| Financial | 5      | Banking and money            |
| Util      | 3      | Common utilities             |
| **Base**  | **2**  | **For custom suppliers**     |
| **Total** | **43** |                              |

## Next Steps

- **[Builder API]({{< ref "builder-api.md" >}})** - Learn how to use suppliers with builders
- **[Examples]({{< ref "examples.md" >}})** - See real-world supplier usage
- **[API Reference]({{< ref "api-reference.md" >}})** - Complete API documentation
- **[Advanced Usage]({{< ref "advanced-usage.md" >}})** - Complex scenarios and patterns

