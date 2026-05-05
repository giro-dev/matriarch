package dev.agiro.matriarch;

import dev.agiro.matriarch.domain.model.Overrider;
import dev.agiro.matriarch.junit.annotations.OverrideField;
import dev.agiro.matriarch.util.OverrideUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Edge-case coverage for {@link OverrideUtils} and the JSON-based
 * override path.
 *
 * <p>Notably this exercises the array-index path: the JSON flattener
 * must produce keys that the collection generators ({@code ListGenerator},
 * {@code SetGenerator}, {@code ArrayGenerator}, {@code MapGenerator})
 * actually recognize. They look for {@code parent[i]}, so the flattener
 * must use the same coordinate format.
 */
class JsonOverrideEdgeCaseTest {

    @Test
    @DisplayName("null and blank jsonOverrides return an empty array map")
    void nullAndBlankJson() {
        Map<String, Overrider> m1 = OverrideUtils.computeOverrideDefinitions(new OverrideField[0], null, true);
        Map<String, Overrider> m2 = OverrideUtils.computeOverrideDefinitions(new OverrideField[0], "", true);
        Map<String, Overrider> m3 = OverrideUtils.computeOverrideDefinitions(new OverrideField[0], "   ", true);
        assertTrue(m1.isEmpty());
        assertTrue(m2.isEmpty());
        assertTrue(m3.isEmpty());
    }

    @Test
    @DisplayName("flat object becomes simple coordinates")
    void flatObject() throws Exception {
        String json = """
                { "name": "Ada", "age": 42, "active": true }
                """;
        Map<String, Overrider> m = OverrideUtils.flattenJsonNodes(new com.fasterxml.jackson.databind.ObjectMapper().readTree(json));
        assertEquals(3, m.size());
        assertEquals("Ada", m.get("name").value());
        assertEquals(Overrider.OverriderType.STRING, m.get("name").type());
        assertEquals("42", m.get("age").value());
        assertEquals(Overrider.OverriderType.OBJECT, m.get("age").type());
        assertEquals("true", m.get("active").value());
        assertEquals(Overrider.OverriderType.OBJECT, m.get("active").type());
    }

    @Test
    @DisplayName("nested objects use dot-notation coordinates")
    void nestedObject() throws Exception {
        String json = """
                { "user": { "address": { "city": "Paris" } } }
                """;
        Map<String, Overrider> m = OverrideUtils.flattenJsonNodes(new com.fasterxml.jackson.databind.ObjectMapper().readTree(json));
        assertEquals(1, m.size());
        assertEquals("Paris", m.get("user.address.city").value());
    }

    @Test
    @DisplayName("array elements use 'parent[i]' (no separating dot)")
    void arrayCoordinatesUseBracketsNoDot() throws Exception {
        // Regression: the prior implementation used "parent.[i]" which
        // List/Set/Array/Map generators do not recognise, so JSON-based
        // index overrides were silently dropped.
        String json = """
                { "items": ["a", "b", "c"] }
                """;
        Map<String, Overrider> m = OverrideUtils.flattenJsonNodes(new com.fasterxml.jackson.databind.ObjectMapper().readTree(json));
        assertTrue(m.containsKey("items[0]"), "expected items[0] but got " + m.keySet());
        assertTrue(m.containsKey("items[1]"));
        assertTrue(m.containsKey("items[2]"));
        assertFalse(m.containsKey("items.[0]"), "should NOT use the dot-bracket form");
    }

    @Test
    @DisplayName("indexed element override reaches the list generator end-to-end")
    void indexedOverrideEndToEnd() {
        // Via the fluent Builder API — this is the canonical happy path
        // that ListGenerator uses to size the list (max index + 1).
        ContainerWithList obj = dev.agiro.matriarch.domain.core.Mother
                .forClass(ContainerWithList.class)
                .forField("list[4]", "pinned")
                .build();
        assertNotNull(obj.list);
        assertTrue(obj.list.size() >= 5, "list should have at least 5 elements, got " + obj.list.size());
        assertEquals("pinned", obj.list.get(4));
    }

    @Test
    @DisplayName("jsonOverrides wins over per-field overrides when jsonWins=true")
    void jsonWinsPrecedence() {
        OverrideField fieldOverride = makeOverrideField("name", "FromArray", false);
        Map<String, Overrider> m = OverrideUtils.computeOverrideDefinitions(
                new OverrideField[]{fieldOverride},
                """
                { "name": "FromJson" }
                """,
                true);
        assertEquals("FromJson", m.get("name").value());
    }

    @Test
    @DisplayName("per-field overrides win when jsonWins=false")
    void arrayWinsPrecedence() {
        OverrideField fieldOverride = makeOverrideField("name", "FromArray", false);
        Map<String, Overrider> m = OverrideUtils.computeOverrideDefinitions(
                new OverrideField[]{fieldOverride},
                """
                { "name": "FromJson" }
                """,
                false);
        assertEquals("FromArray", m.get("name").value());
    }

    @Test
    @DisplayName("invalid JSON throws ParameterResolutionException")
    void invalidJsonThrows() {
        assertThrows(org.junit.jupiter.api.extension.ParameterResolutionException.class,
                () -> OverrideUtils.computeOverrideDefinitions(new OverrideField[0], "{not valid", true));
    }

    @Test
    @DisplayName("isRegex flag wins over the 'type' property when set")
    void isRegexFlagBeatsType() {
        OverrideField field = makeOverrideField("phone", "\\d{3}", true);
        Map<String, Overrider> m = OverrideUtils.flattenOverridesArray(new OverrideField[]{field});
        assertEquals(Overrider.OverriderType.REGEX, m.get("phone").type());
    }

    static class ContainerWithList {
        java.util.List<String> list;
    }

    /** Build an OverrideField at runtime — annotations cannot be instantiated, so use a proxy. */
    private static OverrideField makeOverrideField(String field, String value, boolean isRegex) {
        return new OverrideField() {
            @Override public Class<? extends java.lang.annotation.Annotation> annotationType() { return OverrideField.class; }
            @Override public String field() { return field; }
            @Override public String value() { return value; }
            @Override public Overrider.OverriderType type() { return Overrider.OverriderType.STRING; }
            @Override public boolean isRegex() { return isRegex; }
            @SuppressWarnings({"unchecked", "rawtypes"})
            @Override public Class<? extends java.util.function.Supplier<?>> supplier() {
                return (Class) dev.agiro.matriarch.junit.annotations.internal.NoSupplier.class;
            }
        };
    }
}
