package dev.agiro.matriarch.suppliers;

import dev.agiro.matriarch.suppliers.datetime.*;
import dev.agiro.matriarch.suppliers.numeric.*;
import dev.agiro.matriarch.suppliers.util.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the final set of suppliers (datetime, numeric, util).
 */
class FinalSuppliersTest {

    // DateTime Suppliers
    @Test
    void testPastDateSupplier() {
        PastDateSupplier supplier = new PastDateSupplier();
        Date date = supplier.get();

        assertNotNull(date);
        assertTrue(date.before(new Date()) || date.equals(new Date()));
    }

    @Test
    void testPastDateSupplierCustomRange() {
        PastDateSupplier supplier = new PastDateSupplier(30);
        Date date = supplier.get();

        assertNotNull(date);
        assertTrue(date.before(new Date()) || date.equals(new Date()));
    }

    @Test
    void testFutureDateSupplier() {
        FutureDateSupplier supplier = new FutureDateSupplier();
        Date date = supplier.get();

        assertNotNull(date);
        assertTrue(date.after(new Date()) || date.equals(new Date()));
    }

    @Test
    void testFutureDateSupplierCustomRange() {
        FutureDateSupplier supplier = new FutureDateSupplier(30);
        Date date = supplier.get();

        assertNotNull(date);
        assertTrue(date.after(new Date()) || date.equals(new Date()));
    }

    @Test
    void testRecentDateSupplier() {
        RecentDateSupplier supplier = new RecentDateSupplier();
        Date date = supplier.get();

        assertNotNull(date);
        assertTrue(date.before(new Date()) || date.equals(new Date()));

        // Should be within last 7 days by default
        long sevenDaysAgo = System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000);
        assertTrue(date.getTime() >= sevenDaysAgo - 1000); // Allow 1 second tolerance
    }

    @Test
    void testFutureLocalDateSupplier() {
        FutureLocalDateSupplier supplier = new FutureLocalDateSupplier();
        LocalDate date = supplier.get();

        assertNotNull(date);
        assertTrue(date.isAfter(LocalDate.now()) || date.isEqual(LocalDate.now()));
    }

    @Test
    void testFutureLocalDateSupplierCustomRange() {
        FutureLocalDateSupplier supplier = new FutureLocalDateSupplier(90);
        LocalDate date = supplier.get();

        assertNotNull(date);
        assertTrue(date.isAfter(LocalDate.now()) || date.isEqual(LocalDate.now()));
        assertTrue(date.isBefore(LocalDate.now().plusDays(91)));
    }

    @Test
    void testTimeZoneSupplier() {
        TimeZoneSupplier supplier = new TimeZoneSupplier();
        String timezone = supplier.get();

        assertNotNull(timezone);
        assertFalse(timezone.isEmpty());
        // Should be a valid timezone format
        assertTrue(timezone.contains("/") || timezone.equals("UTC"));
    }

    // Numeric Suppliers
    @Test
    void testPositiveIntegerSupplierDefault() {
        PositiveIntegerSupplier supplier = new PositiveIntegerSupplier();
        Integer value = supplier.get();

        assertNotNull(value);
        assertTrue(value >= 1);
        assertTrue(value < 1000);
    }

    @Test
    void testPositiveIntegerSupplierCustomRange() {
        PositiveIntegerSupplier supplier = new PositiveIntegerSupplier(100, 200);
        Integer value = supplier.get();

        assertNotNull(value);
        assertTrue(value >= 100);
        assertTrue(value < 200);
    }

    @Test
    void testPercentageSupplier() {
        PercentageSupplier supplier = new PercentageSupplier();
        Integer percentage = supplier.get();

        assertNotNull(percentage);
        assertTrue(percentage >= 0);
        assertTrue(percentage <= 100);
    }

    @Test
    void testRatingSupplierDefault() {
        RatingSupplier supplier = new RatingSupplier();
        Integer rating = supplier.get();

        assertNotNull(rating);
        assertTrue(rating >= 1);
        assertTrue(rating <= 5);
    }

    @Test
    void testRatingSupplierCustomRange() {
        RatingSupplier supplier = new RatingSupplier(0, 10);
        Integer rating = supplier.get();

        assertNotNull(rating);
        assertTrue(rating >= 0);
        assertTrue(rating <= 10);
    }

    @Test
    void testVersionSupplier() {
        VersionSupplier supplier = new VersionSupplier();
        String version = supplier.get();

        assertNotNull(version);
        assertTrue(version.matches("\\d+\\.\\d+\\.\\d+"));

        String[] parts = version.split("\\.");
        assertEquals(3, parts.length);
    }

    // Util Suppliers
    @Test
    void testBooleanSupplier() {
        BooleanSupplier supplier = new BooleanSupplier();
        Boolean value = supplier.get();

        assertNotNull(value);
        // Value is a boolean, so no further assertion needed
    }

    @Test
    void testBooleanSupplierVariety() {
        BooleanSupplier supplier = new BooleanSupplier();

        // Generate multiple values to ensure we get variety
        boolean hasTrue = false;
        boolean hasFalse = false;

        for (int i = 0; i < 100; i++) {
            Boolean value = supplier.get();
            if (value) hasTrue = true;
            if (!value) hasFalse = true;
            if (hasTrue && hasFalse) break;
        }

        // Should have both true and false in 100 iterations
        assertTrue(hasTrue);
        assertTrue(hasFalse);
    }

    @Test
    void testStatusSupplier() {
        StatusSupplier supplier = new StatusSupplier();
        String status = supplier.get();

        assertNotNull(status);
        assertFalse(status.isEmpty());
        assertTrue(status.matches("[A-Z_]+"));
    }

    @Test
    void testPrioritySupplier() {
        PrioritySupplier supplier = new PrioritySupplier();
        String priority = supplier.get();

        assertNotNull(priority);
        assertFalse(priority.isEmpty());
        assertTrue(priority.matches("[A-Z_]+"));
    }

    @Test
    void testSuppliersDifferentValues() {
        VersionSupplier versionSupplier = new VersionSupplier();
        StatusSupplier statusSupplier = new StatusSupplier();

        String version1 = versionSupplier.get();
        String version2 = versionSupplier.get();
        String status1 = statusSupplier.get();
        String status2 = statusSupplier.get();

        // At least one pair should be different (high probability)
        assertTrue(!version1.equals(version2) || !status1.equals(status2));
    }
}

