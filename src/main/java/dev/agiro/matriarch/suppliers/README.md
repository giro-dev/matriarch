# Matriarch Suppliers

This package contains ready-to-use `Supplier` implementations for generating realistic test data in Matriarch.

## Package Structure

```
suppliers/
├── base/                    - Base classes for creating custom suppliers
│   ├── RandomSupplier      - Base with random utilities
│   └── ConfigurableSupplier - Base for configurable suppliers
├── personal/                - Personal information
├── address/                 - Location and address data
├── company/                 - Business/company data
├── financial/               - Financial data
├── internet/                - Internet/tech related data
├── text/                    - Text content generators
├── datetime/                - Date and time suppliers
├── numeric/                 - Numbers and identifiers
└── util/                    - Utility suppliers
```

## Usage

### With @OverrideField Annotation

```java

@ParameterizedTest
@MotherSource(
        targetClass = User.class,
        count = 5,
        overrides = {
                @OverrideField(field = "email", supplier = EmailSupplier.class),
                @OverrideField(field = "name", supplier = FullNameSupplier.class),
                @OverrideField(field = "age", supplier = AgeSupplier.class)
        }
)
void testWithSuppliers(User user) {
    assertNotNull(user.getEmail());
    assertTrue(user.getEmail().contains("@"));
    assertNotNull(user.getName());
}
```

### With Builder Pattern

```java
User user = Mother.forClass(User.class)
        .forField("email", new EmailSupplier())
        .forField("fullName", new FullNameSupplier())
        .forField("companyName", new CompanyNameSupplier())
        .forField("registrationDate", new PastLocalDateSupplier())
        .build();
```

### Direct Instantiation

```java
EmailSupplier emailSupplier = new EmailSupplier();
String email1 = emailSupplier.get(); // e.g., "john.smith@gmail.com"
String email2 = emailSupplier.get(); // e.g., "mary456@yahoo.com"
```

## Available Suppliers

### Personal (`personal/`)

- **FirstNameSupplier** - Random first names
- **LastNameSupplier** - Random last names
- **FullNameSupplier** - Full names (first + last)
- **EmailSupplier** - Email addresses with various patterns
- **PhoneSupplier** - US phone numbers in (XXX) XXX-XXXX format
- **UsernameSupplier** - Creative usernames
- **AgeSupplier** - Ages (default 18-80, configurable)
- **DateOfBirthSupplier** - Date of birth (default 18-80 years old, configurable)
- **GenderSupplier** - Gender values

### Company (`company/`)

- **CompanyNameSupplier** - Company names with various patterns
- **CompanySuffixSupplier** - Company suffixes (Inc., LLC, Corp., etc.)
- **JobTitleSupplier** - Job titles and positions
- **DepartmentSupplier** - Department names
- **IndustrySupplier** - Industry names

### Address (`address/`)

- **CitySupplier** - US city names
- **StreetAddressSupplier** - Street addresses
- **StateSupplier** - US states, German Bundesländer, and Spanish provinces
- **ZipCodeSupplier** - ZIP codes (5-digit or ZIP+4 format)
- **CountrySupplier** - Country names
- **CountryCodeSupplier** - ISO 3166-1 alpha-2 country codes
- **LatitudeSupplier** - Latitude coordinates (-90 to 90)
- **LongitudeSupplier** - Longitude coordinates (-180 to 180)
- **CoordinatesSupplier** - Formatted coordinates (latitude, longitude)

### Internet (`internet/`)

- **UrlSupplier** - HTTP/HTTPS URLs
- **DomainSupplier** - Domain names with various TLDs
- **IpV4AddressSupplier** - IPv4 addresses
- **IpV6AddressSupplier** - IPv6 addresses
- **MacAddressSupplier** - MAC addresses
- **PasswordSupplier** - Secure passwords (configurable length and complexity)
- **UserAgentSupplier** - Browser user agent strings
- **SlugSupplier** - URL-friendly slugs

### Text (`text/`)

- **LoremIpsumSupplier** - Lorem ipsum text (configurable word count)
- **WordSupplier** - Random words
- **SentenceSupplier** - Random sentences (configurable word count)
- **ParagraphSupplier** - Random paragraphs (configurable sentence count)
- **TitleSupplier** - Book/article titles

### DateTime (`datetime/`)

- **PastLocalDateSupplier** - Past dates (default 365 days, configurable)

### Numeric (`numeric/`)

- **UuidSupplier** - Random UUIDs

### Financial (`financial/`)

- **PriceSupplier** - Prices as BigDecimal (default 1.00-999.99, configurable)
- **CreditCardSupplier** - Credit card numbers (test data only)
- **IbanSupplier** - IBAN numbers (test data only)
- **CurrencyCodeSupplier** - ISO 4217 currency codes
- **AccountNumberSupplier** - Bank account numbers

### Util (`util/`)

- **BooleanSupplier** - Random boolean values
- **StatusSupplier** - Status values (ACTIVE, INACTIVE, PENDING, etc.)
- **PrioritySupplier** - Priority values (LOW, MEDIUM, HIGH, CRITICAL, URGENT)

## Creating Custom Suppliers

### Simple Supplier

Extend `RandomSupplier<T>` for basic random generation:

```java
public class StatusSupplier extends RandomSupplier<String> {
    private static final String[] STATUSES = {"ACTIVE", "INACTIVE", "PENDING"};

    @Override
    public String get() {
        return randomElement(STATUSES);
    }
}
```

### Configurable Supplier

Extend `ConfigurableSupplier<T>` when you need constructor parameters:

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

## Helper Methods in RandomSupplier

- `randomElement(T[] array)` - Select random element from array
- `randomInt(int min, int max)` - Random integer in range [min, max)
- `RANDOM` - Thread-safe Random instance

## Best Practices

1. **Stateless**: Keep suppliers stateless when possible
2. **No-arg Constructor**: Always provide a public no-arg constructor for annotation usage
3. **Validation**: Validate constructor parameters in configurable suppliers
4. **Javadoc**: Document the default behavior and configuration options
5. **Realistic Data**: Generate data that resembles real-world values
6. **Thread-Safe**: Use the provided `RANDOM` instance from `RandomSupplier`

## Future Suppliers

All core suppliers have been implemented! Consider extending based on your domain needs:

- **International phone formats** - Country-specific phone number formats
- **Product suppliers** - Product names, SKUs, barcodes, product categories
- **Document suppliers** - ISBN, file extensions, MIME types
- **Locale suppliers** - Locale codes, language codes
- **Custom domain-specific suppliers** - Based on your application needs

You can easily create custom suppliers by extending `RandomSupplier` or `ConfigurableSupplier`.

