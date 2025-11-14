package dev.agiro.matriarch.suppliers;

import dev.agiro.matriarch.suppliers.address.*;
import dev.agiro.matriarch.suppliers.company.*;
import dev.agiro.matriarch.suppliers.financial.*;
import dev.agiro.matriarch.suppliers.internet.*;
import dev.agiro.matriarch.suppliers.personal.*;
import dev.agiro.matriarch.suppliers.text.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for all newly added suppliers.
 */
class NewSuppliersTest {

    // Personal Suppliers
    @Test
    void testDateOfBirthSupplier() {
        DateOfBirthSupplier supplier = new DateOfBirthSupplier();
        LocalDate dob = supplier.get();

        assertNotNull(dob);
        assertTrue(dob.isBefore(LocalDate.now()));

        // Check it's within expected age range (18-80 years)
        LocalDate minDate = LocalDate.now().minusYears(81);
        LocalDate maxDate = LocalDate.now().minusYears(17);
        assertTrue(dob.isAfter(minDate) && dob.isBefore(maxDate));
    }

    @Test
    void testGenderSupplier() {
        GenderSupplier supplier = new GenderSupplier();
        String gender = supplier.get();

        assertNotNull(gender);
        assertFalse(gender.isEmpty());
    }

    // Address Suppliers
    @Test
    void testStreetAddressSupplier() {
        StreetAddressSupplier supplier = new StreetAddressSupplier();
        String address = supplier.get();

        assertNotNull(address);
        assertTrue(address.matches("\\d+ .+ (Street|Avenue|Boulevard|Road|Drive|Lane|Way|Court|Place|Terrace|Circle|Parkway)"));
    }

    @Test
    void testStateSupplier() {
        StateSupplier supplier = new StateSupplier();
        String state = supplier.get();

        assertNotNull(state);
        assertFalse(state.isEmpty());
        // Should return US state, German state, or Spanish province
    }

    @Test
    void testZipCodeSupplier() {
        ZipCodeSupplier supplier = new ZipCodeSupplier();
        String zipCode = supplier.get();

        assertNotNull(zipCode);
        assertTrue(zipCode.matches("\\d{5}(-\\d{4})?"));
    }

    @Test
    void testCountrySupplier() {
        CountrySupplier supplier = new CountrySupplier();
        String country = supplier.get();

        assertNotNull(country);
        assertFalse(country.isEmpty());
    }

    @Test
    void testCountryCodeSupplier() {
        CountryCodeSupplier supplier = new CountryCodeSupplier();
        String code = supplier.get();

        assertNotNull(code);
        assertEquals(2, code.length());
        assertTrue(code.matches("[A-Z]{2}"));
    }

    @Test
    void testLatitudeSupplier() {
        LatitudeSupplier supplier = new LatitudeSupplier();
        Double latitude = supplier.get();

        assertNotNull(latitude);
        assertTrue(latitude >= -90.0 && latitude <= 90.0);
    }

    @Test
    void testLongitudeSupplier() {
        LongitudeSupplier supplier = new LongitudeSupplier();
        Double longitude = supplier.get();

        assertNotNull(longitude);
        assertTrue(longitude >= -180.0 && longitude <= 180.0);
    }

    @Test
    void testCoordinatesSupplier() {
        CoordinatesSupplier supplier = new CoordinatesSupplier();
        String coords = supplier.get();

        assertNotNull(coords);
        assertTrue(coords.matches("-?\\d+\\.\\d+, -?\\d+\\.\\d+"));
    }

    // Company Suppliers
    @Test
    void testCompanySuffixSupplier() {
        CompanySuffixSupplier supplier = new CompanySuffixSupplier();
        String suffix = supplier.get();

        assertNotNull(suffix);
        assertFalse(suffix.isEmpty());
    }

    @Test
    void testJobTitleSupplier() {
        JobTitleSupplier supplier = new JobTitleSupplier();
        String jobTitle = supplier.get();

        assertNotNull(jobTitle);
        assertFalse(jobTitle.isEmpty());
    }

    @Test
    void testDepartmentSupplier() {
        DepartmentSupplier supplier = new DepartmentSupplier();
        String department = supplier.get();

        assertNotNull(department);
        assertFalse(department.isEmpty());
    }

    @Test
    void testIndustrySupplier() {
        IndustrySupplier supplier = new IndustrySupplier();
        String industry = supplier.get();

        assertNotNull(industry);
        assertFalse(industry.isEmpty());
    }

    // Financial Suppliers
    @Test
    void testCreditCardSupplier() {
        CreditCardSupplier supplier = new CreditCardSupplier();
        String creditCard = supplier.get();

        assertNotNull(creditCard);
        assertTrue(creditCard.matches("\\d{4} \\d{4} \\d{4} \\d{4}") ||
                creditCard.matches("\\d{4} \\d{6} \\d{5}"));  // Amex format
    }

    @Test
    void testIbanSupplier() {
        IbanSupplier supplier = new IbanSupplier();
        String iban = supplier.get();

        assertNotNull(iban);
        assertTrue(iban.matches("[A-Z]{2}\\d{2}( \\d{4}){4,5}"));
    }

    @Test
    void testCurrencyCodeSupplier() {
        CurrencyCodeSupplier supplier = new CurrencyCodeSupplier();
        String currencyCode = supplier.get();

        assertNotNull(currencyCode);
        assertEquals(3, currencyCode.length());
        assertTrue(currencyCode.matches("[A-Z]{3}"));
    }

    @Test
    void testAccountNumberSupplier() {
        AccountNumberSupplier supplier = new AccountNumberSupplier();
        String accountNumber = supplier.get();

        assertNotNull(accountNumber);
        assertTrue(accountNumber.matches("\\d{10,12}"));
    }

    // Internet Suppliers
    @Test
    void testDomainSupplier() {
        DomainSupplier supplier = new DomainSupplier();
        String domain = supplier.get();

        assertNotNull(domain);
        assertTrue(domain.contains("."));
    }

    @Test
    void testIpV4AddressSupplier() {
        IpV4AddressSupplier supplier = new IpV4AddressSupplier();
        String ip = supplier.get();

        assertNotNull(ip);
        assertTrue(ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"));
    }

    @Test
    void testIpV6AddressSupplier() {
        IpV6AddressSupplier supplier = new IpV6AddressSupplier();
        String ipv6 = supplier.get();

        assertNotNull(ipv6);
        assertTrue(ipv6.matches("[0-9a-f:]+"));
        assertEquals(7, ipv6.chars().filter(ch -> ch == ':').count());
    }

    @Test
    void testMacAddressSupplier() {
        MacAddressSupplier supplier = new MacAddressSupplier();
        String mac = supplier.get();

        assertNotNull(mac);
        assertTrue(mac.matches("[0-9A-F]{2}(:[0-9A-F]{2}){5}"));
    }

    @Test
    void testPasswordSupplier() {
        PasswordSupplier supplier = new PasswordSupplier();
        String password = supplier.get();

        assertNotNull(password);
        assertEquals(12, password.length());
        assertTrue(password.matches(".*[a-z].*"));  // Contains lowercase
        assertTrue(password.matches(".*[A-Z].*"));  // Contains uppercase
        assertTrue(password.matches(".*\\d.*"));    // Contains digit
    }

    @Test
    void testPasswordSupplierCustomLength() {
        PasswordSupplier supplier = new PasswordSupplier(20);
        String password = supplier.get();

        assertNotNull(password);
        assertEquals(20, password.length());
    }

    @Test
    void testUserAgentSupplier() {
        UserAgentSupplier supplier = new UserAgentSupplier();
        String userAgent = supplier.get();

        assertNotNull(userAgent);
        assertTrue(userAgent.contains("Mozilla"));
    }

    @Test
    void testSlugSupplier() {
        SlugSupplier supplier = new SlugSupplier();
        String slug = supplier.get();

        assertNotNull(slug);
        assertTrue(slug.matches("[a-z]+(-[a-z]+)+"));
    }

    // Text Suppliers
    @Test
    void testWordSupplier() {
        WordSupplier supplier = new WordSupplier();
        String word = supplier.get();

        assertNotNull(word);
        assertFalse(word.isEmpty());
        assertTrue(word.matches("[a-z]+"));
    }

    @Test
    void testSentenceSupplier() {
        SentenceSupplier supplier = new SentenceSupplier();
        String sentence = supplier.get();

        assertNotNull(sentence);
        assertTrue(sentence.endsWith("."));
        assertTrue(Character.isUpperCase(sentence.charAt(0)));
    }

    @Test
    void testParagraphSupplier() {
        ParagraphSupplier supplier = new ParagraphSupplier();
        String paragraph = supplier.get();

        assertNotNull(paragraph);
        assertTrue(paragraph.contains("."));
        assertTrue(Character.isUpperCase(paragraph.charAt(0)));
    }

    @Test
    void testTitleSupplier() {
        TitleSupplier supplier = new TitleSupplier();
        String title = supplier.get();

        assertNotNull(title);
        assertFalse(title.isEmpty());
        assertTrue(Character.isUpperCase(title.charAt(0)));
    }

    @Test
    void testMultipleCallsProduceDifferentValues() {
        EmailSupplier emailSupplier = new EmailSupplier();
        IpV4AddressSupplier ipSupplier = new IpV4AddressSupplier();

        String email1 = emailSupplier.get();
        String email2 = emailSupplier.get();
        String ip1 = ipSupplier.get();
        String ip2 = ipSupplier.get();

        // While technically they could be the same, probability is extremely low
        assertNotEquals(email1, email2);
        assertNotEquals(ip1, ip2);
    }
}

