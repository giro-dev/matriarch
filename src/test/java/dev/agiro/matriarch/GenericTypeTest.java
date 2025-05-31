package dev.agiro.matriarch;

import dev.agiro.matriarch.domain.core.Mother;
import dev.agiro.matriarch.object_samples.SimpleRecord;
import dev.agiro.matriarch.object_samples.generics.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Instant;

class GenericTypeTest {

    @Test
    @DisplayName("Should generate object with generic fields having concrete type arguments (Box<String>, Box<Integer>)")
    void testGenericFields_Box_StringAndInteger() {
        GenericWrapper wrapper = Mother.forClass(GenericWrapper.class).build();

        assertNotNull(wrapper);

        assertNotNull(wrapper.getStringBox(), "Box<String> should be instantiated");
        assertNotNull(wrapper.getStringBox().getContent(), "Content of Box<String> should be populated");
        assertTrue(wrapper.getStringBox().getContent() instanceof String, "Content of Box<String> should be a String");

        assertNotNull(wrapper.getIntegerBox(), "Box<Integer> should be instantiated");
        assertNotNull(wrapper.getIntegerBox().getContent(), "Content of Box<Integer> should be populated");
        assertTrue(wrapper.getIntegerBox().getContent() instanceof Integer, "Content of Box<Integer> should be an Integer");
    }

    @Test
    @DisplayName("Should generate object with generic fields (Pair<String, SimpleRecord>)")
    void testGenericFields_Pair_StringSimpleRecord() {
        GenericWrapper wrapper = Mother.forClass(GenericWrapper.class).build();

        assertNotNull(wrapper);
        assertNotNull(wrapper.getStringSimpleRecordPair(), "Pair<String, SimpleRecord> should be instantiated");

        assertNotNull(wrapper.getStringSimpleRecordPair().getKey(), "Key of Pair should be populated");
        assertTrue(wrapper.getStringSimpleRecordPair().getKey() instanceof String, "Key of Pair should be a String");

        assertNotNull(wrapper.getStringSimpleRecordPair().getValue(), "Value of Pair should be populated");
        assertTrue(wrapper.getStringSimpleRecordPair().getValue() instanceof SimpleRecord, "Value of Pair should be a SimpleRecord");
        assertNotNull(wrapper.getStringSimpleRecordPair().getValue().name(), "SimpleRecord's name in Pair should be populated");
    }

    @Test
    @DisplayName("Should generate object with bounded generic type fields (NumberBox<Double>)")
    void testGenericFields_NumberBox_Double() {
        GenericWrapper wrapper = Mother.forClass(GenericWrapper.class).build();

        assertNotNull(wrapper);
        assertNotNull(wrapper.getDoubleNumberBox(), "NumberBox<Double> should be instantiated");
        assertNotNull(wrapper.getDoubleNumberBox().getNumberContent(), "Content of NumberBox<Double> should be populated");
        assertTrue(wrapper.getDoubleNumberBox().getNumberContent() instanceof Double, "Content of NumberBox<Double> should be a Double");
    }

    @Test
    @DisplayName("Should override field inside a generic Box<String>")
    void testOverride_FieldInGenericBoxString() {
        String overriddenString = "Overridden Generic String";
        GenericWrapper wrapper = Mother.forClass(GenericWrapper.class)
                .override("stringBox.content", overriddenString)
                .build();

        assertNotNull(wrapper);
        assertNotNull(wrapper.getStringBox());
        assertEquals(overriddenString, wrapper.getStringBox().getContent());
    }

    @Test
    @DisplayName("Should override fields in a generic Pair<String, SimpleRecord>")
    void testOverride_FieldsInGenericPair() {
        String overriddenKey = "Overridden Pair Key";
        String overriddenSimpleRecordName = "Overridden Record Name in Pair";
        int overriddenSimpleRecordValue = 12345;
        Instant overriddenTimestamp = Instant.now();


        GenericWrapper wrapper = Mother.forClass(GenericWrapper.class)
                .override("stringSimpleRecordPair.key", overriddenKey)
                .override("stringSimpleRecordPair.value.name", overriddenSimpleRecordName)
                .override("stringSimpleRecordPair.value.value", overriddenSimpleRecordValue)
                .override("stringSimpleRecordPair.value.createdAt", overriddenTimestamp)
                .build();

        assertNotNull(wrapper);
        assertNotNull(wrapper.getStringSimpleRecordPair());
        assertEquals(overriddenKey, wrapper.getStringSimpleRecordPair().getKey());

        SimpleRecord sr = wrapper.getStringSimpleRecordPair().getValue();
        assertNotNull(sr);
        assertEquals(overriddenSimpleRecordName, sr.name());
        assertEquals(overriddenSimpleRecordValue, sr.value());
        assertEquals(overriddenTimestamp, sr.createdAt());
    }

    @Test
    @DisplayName("Should override field in a bounded generic NumberBox<Double>")
    void testOverride_FieldInBoundedGenericNumberBox() {
        Double overriddenDouble = 123.456;
        GenericWrapper wrapper = Mother.forClass(GenericWrapper.class)
                .override("doubleNumberBox.numberContent", overriddenDouble)
                .build();

        assertNotNull(wrapper);
        assertNotNull(wrapper.getDoubleNumberBox());
        assertEquals(overriddenDouble, wrapper.getDoubleNumberBox().getNumberContent(), 0.001);
    }

    @Test
    @DisplayName("Should allow providing a fully constructed generic object for a field")
    void testOverride_ProvideFullGenericObject() {
        Box<String> customBox = new Box<>("Custom Content For Box");

        GenericWrapper wrapper = Mother.forClass(GenericWrapper.class)
                .override("stringBox", customBox)
                .build();

        assertNotNull(wrapper);
        assertSame(customBox, wrapper.getStringBox(), "The customBox instance should be the same");
        assertEquals("Custom Content For Box", wrapper.getStringBox().getContent());

        // Ensure other fields are still generated
        assertNotNull(wrapper.getIntegerBox());
        assertNotNull(wrapper.getIntegerBox().getContent());
    }
}
