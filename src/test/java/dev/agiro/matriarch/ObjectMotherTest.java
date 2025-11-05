package dev.agiro.matriarch;

import dev.agiro.matriarch.domain.core.Mother;
import dev.agiro.matriarch.domain.core.Regex;
import dev.agiro.matriarch.object_samples.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class ObjectMotherTest {

    @Test
    @DisplayName("Shoud generate an object with primitivesprimitive ")
    void test_basic_objects_with_constructor() {
        AllArgsConstructorBasicObjectAllTypes basicObject = Mother.forClass(AllArgsConstructorBasicObjectAllTypes.class).build();
        assertAll("All fiels has values",
                  () -> assertNotNull(basicObject.getString()),
                  () -> assertNotNull(basicObject.getInteger()),
                  () -> assertNotNull(basicObject.isBool()),
                  () -> assertNotNull(basicObject.getDecimal()),
                  () -> assertNotNull(basicObject.getFloating()),
                  () -> assertNotNull(basicObject.getInstant()),
                  () -> assertNotNull(basicObject.getLongInt()),
                  () -> assertNotNull(basicObject.getTimestamp()),
                  () -> assertNotNull(basicObject.getCharacter()),
                  () -> assertNotNull(basicObject.getBigDecimal()),
                  () -> assertNotNull(basicObject.getUuid()),
                  () -> assertNotNull(basicObject.getDate()),
                  () -> assertNotNull(basicObject.getNestedObject()),
                  () -> assertNotNull(basicObject.getNestedObject().getString()),
                  () -> assertNotNull(basicObject.getNestedObject().getInteger()),
                  () -> assertFalse(basicObject.getList().isEmpty()),
                  () -> assertFalse(basicObject.getObjectList().isEmpty())
        );
    }

@Test
    @DisplayName("Shoud generate an object with basic types")
    void test_basic_objects_with_constructor_basic() {
        var basicObject = Mother.forClass(AllArgsConstructorBasicObjectBasicTypes.class).build();
        assertAll("All fiels has values",
                  () -> assertNotNull(basicObject.getString()),
                  () -> assertNotNull(basicObject.getInteger()),
                  () -> assertNotNull(basicObject.isBool()),
                  () -> assertNotNull(basicObject.getDecimal()),
                  () -> assertNotNull(basicObject.getFloating()),
                  () -> assertNotNull(basicObject.getInstant()),
                  () -> assertNotNull(basicObject.getLongInt()),
                  () -> assertNotNull(basicObject.getTimestamp()),
                  () -> assertNotNull(basicObject.getCharacter()),
                  () -> assertNotNull(basicObject.getBigDecimal()),
                  () -> assertNotNull(basicObject.getUuid()),
                  () -> assertNotNull(basicObject.getDate()),
                  () -> assertNotNull(basicObject.getNestedObject()),
                  () -> assertNotNull(basicObject.getNestedObject().getString()),
                  () -> assertNotNull(basicObject.getNestedObject().getInteger())
        );
    }

    @DisplayName("Should generate an object with static constructor")
    @Test
    void test_basic_objects_with_static_constructor() {
        StaticConstructorObject staticConstructorObject = Mother.forClass(StaticConstructorObject.class).build();
        assertNotNull(staticConstructorObject.value());
    }

    @DisplayName("Generate Map")
    @Test
    void complexObjects(){
        ContainingParametrizedObjectFields birthed = Mother.forClass(ContainingParametrizedObjectFields.class).birth();
        assertFalse(birthed.map().isEmpty());
        assertTrue(birthed.map().values().stream().allMatch(value -> value instanceof SimpleRecord));
        assertFalse(birthed.timeMap().isEmpty());
        assertTrue(birthed.timeMap().values().stream().allMatch(value -> value instanceof SimpleRecord));
        assertNotNull(birthed.list());
        assertFalse(birthed.list().isEmpty());
    }

    @DisplayName("Override Map")
    @Test
    void complexObjectsOverride(){
        Instant createdAt = YearMonth.of(2025, 1).atEndOfMonth().atStartOfDay().toInstant(ZoneOffset.UTC);
        SimpleRecord simpleRecord = new SimpleRecord("testOK", 12345, createdAt);
        SimpleRecord simpleRecord2 = new SimpleRecord("testOK2", 67890, createdAt);
        ContainingParametrizedObjectFields birthed = Mother.forClass(ContainingParametrizedObjectFields.class)
                .override("map[test]", simpleRecord)
                .override("timeMap[" + createdAt.toString() + "]", simpleRecord2)
                .override("list[40]", simpleRecord2)
                .birth();
        assertNotNull(birthed.map());
        assertTrue(birthed.map().values().stream().allMatch(value -> value instanceof SimpleRecord));
        assertEquals(simpleRecord, birthed.map().get("test"));
        assertEquals(simpleRecord2, birthed.timeMap().get(createdAt));
        assertEquals(simpleRecord2, birthed.list().get(40));
    }

    @DisplayName("Should override known patterns")
    @Test
    void test_override_known_patterns() {
        RecordWithKnownPatterns recordWithKnownPatterns = Mother.forClass(RecordWithKnownPatterns.class)
                .build();
        assertAll("All fields has values",
                  () -> assertTrue(recordWithKnownPatterns.plantId().matches("\\d{4}")),
                  () -> assertTrue(recordWithKnownPatterns.email().matches(
                          "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))
        );
    }

    @DisplayName("Should cast object with known pattern")
    @Test
    void test_cast_object_with_known_pattern() {
        ObjectWithKnlownPatternCasted objectWithKnlownPatternCasted = Mother.forClass(ObjectWithKnlownPatternCasted.class)
                .build();
        assertNotNull(objectWithKnlownPatternCasted.plantId());
    }

    @DisplayName("Should override with builder parameters")
    @Test
    void test_override_with_builder_parameters() {
        AllArgsConstructorBasicObjectAllTypes basicObject = Mother.forClass(AllArgsConstructorBasicObjectAllTypes.class)
                .override("string", "overrided")
                .override("integer", new Regex("1[0-9]{3}"))
                .override("instant", "2021-01-01T00:00:00Z")
                .override("nestedObject.string", "nested_overrided")
                .build();
        assertEquals("overrided", basicObject.getString());
        assertTrue(basicObject.getInteger() >= 1000 && basicObject.getInteger() < 2000,
                   "Integer should be in range [1000, 2000), but was: " + basicObject.getInteger());
        assertEquals("2021-01-01T00:00:00Z", basicObject.getInstant().toString());
        assertEquals("nested_overrided", basicObject.getNestedObject().getString());
    }
}
