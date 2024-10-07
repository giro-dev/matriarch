package dev.agiro.matriarch;

import dev.agiro.matriarch.domain.core.Mother;
import dev.agiro.matriarch.object_samples.AllArgsConstructorBasicObjectAllTypes;
import dev.agiro.matriarch.object_samples.ObjectWithKnlownPatternCasted;
import dev.agiro.matriarch.object_samples.RecordWithKnownPatterns;
import dev.agiro.matriarch.object_samples.StaticConstructorObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectMotherTest {

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

    @DisplayName("Should generate an object with static constructor")
    @Test
    void test_basic_objects_with_static_constructor() {
        StaticConstructorObject staticConstructorObject = Mother.forClass(StaticConstructorObject.class).build();
        assertNotNull(staticConstructorObject.value());
    }

    @DisplayName("Should override known patterns")
    @Test
    void test_override_known_patterns() {
        RecordWithKnownPatterns recordWithKnownPatterns = Mother.forClass(RecordWithKnownPatterns.class)
                .build();
        assertAll("All fields has values",
                  () -> assertTrue(recordWithKnownPatterns.plantId().matches("\\d{4}")),
                  () -> assertTrue(recordWithKnownPatterns.supplier().matches("\\d{8}[A-Z]?")),
                  () -> assertTrue(recordWithKnownPatterns.plantType().matches(
                          "GLC|Other|Powertrain|MBCC|CKD|CBU|Cooperation|Remanufacturing|VAN")),
                  () -> assertTrue(recordWithKnownPatterns.email().matches(
                          "[a-z]{4,8}\\.[a-z]{4,8}_[a-z]{4,8}\\@(mercedes-benz|external-mercedes-benz).com"))
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
                .override("integer", new Mother.Regex("1[0-9]{3}"))
                .override("instant", "2021-01-01T00:00:00Z")
                .override("nestedObject.string", "nested_overrided")
                .build();
        assertEquals("overrided", basicObject.getString());
        assertTrue(basicObject.getInteger() > 1000 && basicObject.getInteger() < 2000);
        assertEquals("2021-01-01T00:00:00Z", basicObject.getInstant().toString());
        assertEquals("nested_overrided", basicObject.getNestedObject().getString());
    }
}
