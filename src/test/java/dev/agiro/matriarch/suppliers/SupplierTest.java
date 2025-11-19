package dev.agiro.matriarch.suppliers;

import dev.agiro.matriarch.suppliers.address.CitySupplier;
import dev.agiro.matriarch.suppliers.company.CompanyNameSupplier;
import dev.agiro.matriarch.suppliers.datetime.PastLocalDateSupplier;
import dev.agiro.matriarch.suppliers.financial.PriceSupplier;
import dev.agiro.matriarch.suppliers.internet.UrlSupplier;
import dev.agiro.matriarch.suppliers.numeric.UuidSupplier;
import dev.agiro.matriarch.suppliers.personal.*;
import dev.agiro.matriarch.suppliers.text.LoremIpsumSupplier;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Demonstration and basic validation tests for all suppliers.
 */
class SupplierTest {

    @Test
    void testFirstNameSupplier() {
        FirstNameSupplier supplier = new FirstNameSupplier();
        String firstName = supplier.get();

        assertNotNull(firstName);
        assertFalse(firstName.isEmpty());
        assertTrue(firstName.matches("[A-Z][a-z]+"));
    }

    @Test
    void testLastNameSupplier() {
        LastNameSupplier supplier = new LastNameSupplier();
        String lastName = supplier.get();

        assertNotNull(lastName);
        assertFalse(lastName.isEmpty());
    }

    @Test
    void testFullNameSupplier() {
        FullNameSupplier supplier = new FullNameSupplier();
        String fullName = supplier.get();

        assertNotNull(fullName);
        assertTrue(fullName.contains(" "));
        String[] parts = fullName.split(" ");
        assertEquals(2, parts.length);
    }

    @Test
    void testEmailSupplier() {
        EmailSupplier supplier = new EmailSupplier();
        String email = supplier.get();

        assertNotNull(email);
        assertTrue(email.contains("@"));
        assertTrue(email.contains("."));
        assertTrue(email.matches("^[a-z0-9._]+@[a-z.]+$"));
    }

    @Test
    void testPhoneSupplier() {
        PhoneSupplier supplier = new PhoneSupplier();
        String phone = supplier.get();

        assertNotNull(phone);
        assertTrue(phone.matches("\\(\\d{3}\\) \\d{3}-\\d{4}"));
    }

    @Test
    void testUsernameSupplier() {
        UsernameSupplier supplier = new UsernameSupplier();
        String username = supplier.get();

        assertNotNull(username);
        assertFalse(username.isEmpty());
    }

    @Test
    void testAgeSupplierDefault() {
        AgeSupplier supplier = new AgeSupplier();
        Integer age = supplier.get();

        assertNotNull(age);
        assertTrue(age >= 18);
        assertTrue(age <= 80);
    }

    @Test
    void testAgeSupplierCustomRange() {
        AgeSupplier supplier = new AgeSupplier(25, 35);
        Integer age = supplier.get();

        assertNotNull(age);
        assertTrue(age >= 25);
        assertTrue(age <= 35);
    }

    @Test
    void testCompanyNameSupplier() {
        CompanyNameSupplier supplier = new CompanyNameSupplier();
        String company = supplier.get();

        assertNotNull(company);
        assertFalse(company.isEmpty());
    }

    @Test
    void testCitySupplier() {
        CitySupplier supplier = new CitySupplier();
        String city = supplier.get();

        assertNotNull(city);
        assertFalse(city.isEmpty());
    }

    @Test
    void testUrlSupplier() {
        UrlSupplier supplier = new UrlSupplier();
        String url = supplier.get();

        assertNotNull(url);
        assertTrue(url.startsWith("http://") || url.startsWith("https://"));
    }

    @Test
    void testUuidSupplier() {
        UuidSupplier supplier = new UuidSupplier();
        String uuid = supplier.get();

        assertNotNull(uuid);
        assertTrue(uuid.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
    }

    @Test
    void testPastLocalDateSupplier() {
        PastLocalDateSupplier supplier = new PastLocalDateSupplier();
        LocalDate date = supplier.get();

        assertNotNull(date);
        assertTrue(date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now()));
    }

    @Test
    void testPastLocalDateSupplierCustomRange() {
        PastLocalDateSupplier supplier = new PastLocalDateSupplier(30);
        LocalDate date = supplier.get();

        assertNotNull(date);
        assertTrue(date.isAfter(LocalDate.now().minusDays(31)));
        assertTrue(date.isBefore(LocalDate.now().plusDays(1)));
    }

    @Test
    void testLoremIpsumSupplier() {
        LoremIpsumSupplier supplier = new LoremIpsumSupplier();
        String text = supplier.get();

        assertNotNull(text);
        assertFalse(text.isEmpty());
        assertTrue(text.endsWith("."));
    }

    @Test
    void testLoremIpsumSupplierCustomWordCount() {
        LoremIpsumSupplier supplier = new LoremIpsumSupplier(10);
        String text = supplier.get();

        assertNotNull(text);
        String[] words = text.replace(".", "").replace(",", "").split("\\s+");
        assertEquals(10, words.length);
    }

    @Test
    void testPriceSupplierDefault() {
        PriceSupplier supplier = new PriceSupplier();
        BigDecimal price = supplier.get();

        assertNotNull(price);
        assertTrue(price.compareTo(BigDecimal.ONE) >= 0);
        assertTrue(price.compareTo(new BigDecimal("999.99")) <= 0);
        assertEquals(2, price.scale());
    }

    @Test
    void testPriceSupplierCustomRange() {
        PriceSupplier supplier = new PriceSupplier(100.0, 200.0);
        BigDecimal price = supplier.get();

        assertNotNull(price);
        assertTrue(price.compareTo(new BigDecimal("100.00")) >= 0);
        assertTrue(price.compareTo(new BigDecimal("200.00")) <= 0);
    }

    @Test
    void testRandomnessAndUniqueness() {
        EmailSupplier supplier = new EmailSupplier();
        String email1 = supplier.get();
        String email2 = supplier.get();

        // While technically they could be the same, the probability is extremely low
        assertNotEquals(email1, email2);
    }
}

