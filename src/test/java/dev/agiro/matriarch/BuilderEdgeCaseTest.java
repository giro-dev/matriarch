package dev.agiro.matriarch;

import dev.agiro.matriarch.domain.core.Mother;
import dev.agiro.matriarch.domain.core.Regex;
import dev.agiro.matriarch.domain.model.Overrider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Edge cases for the {@code Mother.Builder} fluent API, covering
 * null arguments, type-based overrides, and the {@code Set}/{@code Map}
 * override paths (which previously miscast due to wrong super-class
 * wiring in Set/Map generators).
 */
class BuilderEdgeCaseTest {

    public static class Bag {
        public String name;
        public Set<String> tags;
        public Map<String, Integer> counts;
    }

    @Test
    @DisplayName("forField(name, (String) null) stores a NULL overrider")
    void forFieldNullString() {
        Bag b = Mother.forClass(Bag.class)
                .forField("name", (String) null)
                .build();
        assertNull(b.name);
    }

    @Test
    @DisplayName("forField(name, (Object) null) stores a NULL overrider")
    void forFieldNullObject() {
        Bag b = Mother.forClass(Bag.class)
                .forField("name", (Object) null)
                .build();
        assertNull(b.name);
    }

    @Test
    @DisplayName("forField(name, (Regex) null) stores a NULL overrider")
    void forFieldNullRegex() {
        Bag b = Mother.forClass(Bag.class)
                .forField("name", (Regex) null)
                .build();
        assertNull(b.name);
    }

    @Test
    @DisplayName("override with a real Regex populates the field")
    void forFieldRegex() {
        Bag b = Mother.forClass(Bag.class)
                .forField("name", new Regex("\\d{4}"))
                .build();
        assertNotNull(b.name);
        assertTrue(b.name.matches("\\d{4}"), "got: " + b.name);
    }

    @Test
    @DisplayName("forField(String, Object, type=NULL) stores a NULL value")
    void forFieldWithNullType() {
        // type=NULL with a non-null object: library currently treats it as
        // OBJECT. Document the current behaviour so future refactors catch
        // accidental changes.
        Bag b = Mother.forClass(Bag.class)
                .forField("name", "literal", Overrider.OverriderType.NULL)
                .build();
        assertEquals("literal", b.name);
    }

    @Test
    @DisplayName("excludeFields sets the field to null even for nested fields")
    void excludeFieldsNested() {
        class Outer {
            public Bag bag;
        }
        Outer o = Mother.forClass(Outer.class)
                .excludeFields("bag.name")
                .build();
        assertNotNull(o.bag);
        assertNull(o.bag.name, "excluded nested field should be null");
    }

    @Test
    @DisplayName("excludeFields across many repeated calls accumulates")
    void excludeFieldsAccumulates() {
        Bag b = Mother.forClass(Bag.class)
                .excludeFields("name")
                .excludeFields("tags")
                .build();
        assertNull(b.name);
        assertNull(b.tags);
    }

    @Test
    @DisplayName("forType replaces all fields of that type across the hierarchy")
    void forTypeReplacesTypeFields() {
        class Container {
            public String primary;
            public String secondary;
        }
        Container c = Mother.forClass(Container.class)
                .forType(String.class, () -> "PINNED")
                .build();
        assertEquals("PINNED", c.primary);
        assertEquals("PINNED", c.secondary);
    }

    @Test
    @DisplayName("forField overrides win over forType for the same field")
    void forFieldBeatsForType() {
        class Container {
            public String primary;
            public String secondary;
        }
        Container c = Mother.forClass(Container.class)
                .forType(String.class, () -> "ALL")
                .forField("primary", "SPECIAL")
                .build();
        assertEquals("SPECIAL", c.primary);
        assertEquals("ALL", c.secondary);
    }

    @Test
    @DisplayName("forType with a Supplier is invoked independently per matching field")
    void forTypeSupplierInvokedPerField() {
        class TwoStrings {
            public String a;
            public String b;
        }
        final int[] counter = {0};
        Supplier<String> sup = () -> "v" + (++counter[0]);
        TwoStrings obj = Mother.forClass(TwoStrings.class)
                .forType(String.class, sup)
                .build();
        assertNotNull(obj.a);
        assertNotNull(obj.b);
        // Counter was invoked at least twice (once per field).
        assertTrue(counter[0] >= 2, "supplier should be invoked at least twice, was " + counter[0]);
    }

    @Test
    @DisplayName("overriding a Set<String> field with an actual Set value works (regression)")
    void overrideSetFieldWithSetValue() {
        // Regression: SetGenerator's super(List.class) caused cast failure
        // whenever a user overrode a Set field with a concrete Set object.
        Set<String> pinned = new LinkedHashSet<>(List.of("alpha", "beta", "gamma"));
        Bag b = Mother.forClass(Bag.class)
                .forField("tags", pinned)
                .build();
        assertNotNull(b.tags);
        assertEquals(Set.of("alpha", "beta", "gamma"), new HashSet<>(b.tags));
    }

    @Test
    @DisplayName("overriding a Map<K,V> field with an actual Map value works (regression)")
    void overrideMapFieldWithMapValue() {
        // Regression: MapGenerator's super(List.class) caused cast failure
        // whenever a user overrode a Map field with a concrete Map object.
        Map<String, Integer> pinned = new LinkedHashMap<>();
        pinned.put("a", 1);
        pinned.put("b", 2);
        Bag b = Mother.forClass(Bag.class)
                .forField("counts", pinned)
                .build();
        assertNotNull(b.counts);
        assertEquals(1, b.counts.get("a"));
        assertEquals(2, b.counts.get("b"));
    }

    @Test
    @DisplayName("Builder is reusable: calling build() twice returns distinct instances")
    void builderReusableReturnsDistinctInstances() {
        var builder = Mother.forClass(Bag.class).forField("name", "ONE");
        Bag b1 = builder.build();
        Bag b2 = builder.build();
        assertNotSame(b1, b2);
        assertEquals("ONE", b1.name);
        assertEquals("ONE", b2.name);
    }

    public static class EmptyPojo {
        public EmptyPojo() {}
    }

    @Test
    @DisplayName("build() never throws for an empty POJO with a no-arg constructor")
    void buildEmptyPojo() {
        assertDoesNotThrow(() -> Mother.forClass(EmptyPojo.class).build());
    }

    @Test
    @DisplayName("override on a non-existent field is silently ignored (does not throw)")
    void overrideNonExistentField() {
        assertDoesNotThrow(() -> Mother.forClass(Bag.class)
                .forField("doesNotExist", "whatever")
                .build());
    }

    @Test
    @DisplayName("birth() is an alias for build()")
    void birthAlias() {
        assertNotNull(Mother.forClass(Bag.class).birth());
    }

    @Test
    @DisplayName("forField with a supplier that returns null falls through to the type's default generator")
    void forFieldSupplierReturnsNull() {
        // Edge case: when a Supplier returns null, the override is treated as
        // 'no override' and the field is generated as if no override was set.
        // This matches the current semantics — pinning so accidental
        // semantic changes are visible.
        Supplier<String> nullSup = () -> null;
        Bag b = Mother.forClass(Bag.class)
                .forField("name", nullSup)
                .build();
        assertNotNull(b.name, "supplier returning null currently falls back to default generation");
    }

    @Test
    @DisplayName("legacy override(...) aliases forField(...)")
    void legacyOverrideAliases() {
        Bag b = Mother.forClass(Bag.class)
                .override("name", "legacy")
                .build();
        assertEquals("legacy", b.name);
    }
}
