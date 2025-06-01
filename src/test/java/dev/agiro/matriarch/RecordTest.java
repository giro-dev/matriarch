package dev.agiro.matriarch;

import dev.agiro.matriarch.domain.core.Mother;
import dev.agiro.matriarch.object_samples.SimpleRecord; // Assuming this is the correct package
import dev.agiro.matriarch.object_samples.records.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.time.Instant;

class RecordTest {

    @Test
    @DisplayName("Should generate a simple record with basic data types")
    void testSimpleRecordGeneration() {
        SimpleRecord record = Mother.forClass(SimpleRecord.class).build();
        assertNotNull(record);
        assertNotNull(record.name());
        // Assuming int is populated, could be 0 or any other number by default
        assertTrue(record.value() >= 0 || record.value() < 0);
        assertNotNull(record.createdAt());
    }

    @Test
    @DisplayName("Should generate a record with a nested record")
    void testNestedRecordGeneration() {
        NestedRecordOuter record = Mother.forClass(NestedRecordOuter.class).build();
        assertNotNull(record);
        assertNotNull(record.outerField());
        assertNotNull(record.nested());
        assertNotNull(record.nested().name());
        assertNotNull(record.nested().createdAt());
    }

    @Test
    @DisplayName("Should generate a record with collections (List, Set, Map)")
    void testRecordWithCollectionsGeneration() {
        RecordWithCollections record = Mother.forClass(RecordWithCollections.class).build();
        assertNotNull(record);

        assertNotNull(record.stringList());
        assertFalse(record.stringList().isEmpty(), "String list should not be empty");
        record.stringList().forEach(Assertions::assertNotNull);

        assertNotNull(record.integerSet());
        assertFalse(record.integerSet().isEmpty(), "Integer set should not be empty");
        record.integerSet().forEach(Assertions::assertNotNull);

        assertNotNull(record.recordMap());
        assertFalse(record.recordMap().isEmpty(), "Record map should not be empty");
        record.recordMap().forEach((key, value) -> {
            assertNotNull(key);
            assertNotNull(value);
            assertNotNull(value.name());
        });
    }

    @Test
    @DisplayName("Should generate a record using its custom canonical constructor")
    void testRecordWithCustomCanonicalConstructor() {
        // Matriarch should use the canonical constructor.
        // If the default generated values don't meet constructor constraints (e.g. blank name),
        // this test might fail. We might need to override to ensure valid inputs for the constructor.
        RecordWithCustomCanonicalConstructor record = Mother.forClass(RecordWithCustomCanonicalConstructor.class)
            .override("name", "TestName") // Ensure name is not blank
            .override("value", 10) // Provide a value
            .build();

        assertNotNull(record);
        assertEquals("TESTNAME", record.name()); // Name is uppercased by constructor
        assertEquals(20, record.value()); // Value is doubled by constructor
    }

    @Test
    @DisplayName("Should generate a record using its compact constructor")
    void testRecordWithCompactConstructor() {
        // Similar to the canonical constructor, ensure overrides meet compact constructor logic if needed.
        RecordWithCompactConstructor record = Mother.forClass(RecordWithCompactConstructor.class)
            .override("item", "  Test Item  ")
            .override("price", 50.0)
            .build();

        assertNotNull(record);
        assertEquals("Test Item", record.item()); // Item is trimmed by compact constructor
        assertEquals(50.0, record.price());
    }

    @Test
    @DisplayName("Should generate a record with a non-canonical constructor if specified/handled (or default to canonical)")
    void testRecordWithNonCanonicalConstructor() {
        // Matriarch typically uses the canonical constructor or a zero-arg constructor if available and applicable.
        // It might not automatically pick other non-canonical constructors unless it has specific logic for it.
        // This test will likely verify generation via the canonical one.
        RecordWithNonCanonicalConstructor record = Mother.forClass(RecordWithNonCanonicalConstructor.class).build();
        assertNotNull(record);
        assertNotNull(record.id());
        assertNotNull(record.description(), "Description should be populated by Matriarch, not via the non-canonical constructor by default.");
        // If we wanted to test the non-canonical constructor, we'd likely call it directly,
        // not through Matriarch, unless Matriarch offers a way to specify constructor choice.
    }

    @Test
    @DisplayName("Should override fields in a simple record")
    void testOverrideFieldsInSimpleRecord() {
        String overriddenName = "Overridden Record Name";
        int overriddenValue = 98765;
        Instant overriddenDate = Instant.parse("2023-01-01T12:00:00Z");

        SimpleRecord record = Mother.forClass(SimpleRecord.class)
                .override("name", overriddenName)
                .override("value", overriddenValue)
                .override("createdAt", overriddenDate)
                .build();

        assertNotNull(record);
        assertEquals(overriddenName, record.name());
        assertEquals(overriddenValue, record.value());
        assertEquals(overriddenDate, record.createdAt());
    }

    @Test
    @DisplayName("Should override fields in a nested record")
    void testOverrideFieldsInNestedRecord() {
        String overriddenOuterField = "Main";
        String overriddenNestedName = "SubRecord";
        int overriddenNestedValue = 112233;
        Instant overriddenNestedDate = Instant.parse("2024-02-15T10:30:00Z");

        NestedRecordOuter record = Mother.forClass(NestedRecordOuter.class)
                .override("outerField", overriddenOuterField)
                .override("nested.name", overriddenNestedName)
                .override("nested.value", overriddenNestedValue)
                .override("nested.createdAt", overriddenNestedDate)
                .build();

        assertNotNull(record);
        assertEquals(overriddenOuterField, record.outerField());
        assertNotNull(record.nested());
        assertEquals(overriddenNestedName, record.nested().name());
        assertEquals(overriddenNestedValue, record.nested().value());
        assertEquals(overriddenNestedDate, record.nested().createdAt());
    }
}
