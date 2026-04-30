package dev.agiro.matriarch;

import dev.agiro.matriarch.domain.core.Mother;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pinned tests for documented sharp edges in the matriarch library.
 * These behaviours are intentional today (see comments / TODO markers in
 * the source), but have measurable consequences worth pinning so accidental
 * regressions are visible.
 *
 * <p>Each {@link Disabled} test below is a reproducer for an issue the project
 * owner may want to address — they are kept as ready-to-flip fixtures.
 */
class SharpEdgeBehaviourTest {

    public static class WithPrimitives {
        public boolean active;
        public int score;
        public String label;
    }

    @Test
    @DisplayName("excludeFields on a primitive field leaves it at its default value")
    void excludeFieldsOnPrimitive() {
        // The library applies a NULL overrider; reflection cannot set null on a
        // primitive, so the field stays at its language default (false / 0).
        WithPrimitives obj = Mother.forClass(WithPrimitives.class)
                .excludeFields("active", "score", "label")
                .build();
        assertNull(obj.label);
        assertFalse(obj.active);
        assertEquals(0, obj.score);
    }

    public static class WithDate {
        public String name;
        public WithDateInner inner;

        public static class WithDateInner {
            public LocalDate when;
        }
    }

    @Disabled("Sharp edge: forType(...) only walks the inheritance chain, "
            + "not nested fields. See TypeOverrideStrategy.applyTypeOverridesRecursive.")
    @Test
    @DisplayName("[GAP] forType(Class, supplier) should descend into nested objects")
    void forTypeDescendsIntoNestedObjects() {
        LocalDate fixed = LocalDate.of(2024, 1, 1);
        WithDate obj = Mother.forClass(WithDate.class)
                .forType(LocalDate.class, () -> fixed)
                .build();
        // Today this fails because nested fields are not visited.
        assertEquals(fixed, obj.inner.when);
    }

    public static class WithStringArray {
        public String[] tags;
    }

    @Disabled("Sharp edge: ARRAY entry in ClazzGenerators is commented out, "
            + "so array fields are populated as null instead of being generated.")
    @Test
    @DisplayName("[GAP] String[] fields should be populated, not left as null")
    void arraysShouldBePopulated() {
        WithStringArray obj = Mother.forClass(WithStringArray.class).build();
        assertNotNull(obj.tags, "expected array generator to populate the tags field");
        assertTrue(obj.tags.length > 0);
    }

    public static class WithConcreteCollection {
        public LinkedList<String> queue;
        public HashMap<String, Integer> counts;
    }

    @Disabled("Sharp edge: ClazzGenerators.forClass uses contains() — concrete impl "
            + "classes (LinkedList, HashMap, ...) are not registered and fall back to GENERIC.")
    @Test
    @DisplayName("[GAP] Concrete collection impls (LinkedList, HashMap) should be populated")
    void concreteCollectionImplsShouldBePopulated() {
        WithConcreteCollection obj = Mother.forClass(WithConcreteCollection.class).build();
        assertNotNull(obj.queue);
        assertFalse(obj.queue.isEmpty(), "LinkedList<String> should be populated");
        assertNotNull(obj.counts);
        assertFalse(obj.counts.isEmpty(), "HashMap<String, Integer> should be populated");
    }

    public static class WithNestedGeneric {
        public java.util.List<java.util.List<String>> matrix;
    }

    @Disabled("Sharp edge: ListGenerator resolves the element class via "
            + "Class.forName(parametrizedType[0].getTypeName()) which fails for "
            + "nested parameterized types like List<List<String>>.")
    @Test
    @DisplayName("[GAP] List<List<String>> should be populated end-to-end")
    void nestedGenericsShouldBePopulated() {
        WithNestedGeneric obj = Mother.forClass(WithNestedGeneric.class).build();
        assertNotNull(obj.matrix);
        assertFalse(obj.matrix.isEmpty());
        obj.matrix.forEach(row -> {
            assertNotNull(row);
            assertFalse(row.isEmpty());
        });
    }

    @Disabled("Sharp edge: withCollectionSize(...) configuration is stored but never "
            + "applied — collection generators always pick a random size in [1, 15).")
    @Test
    @DisplayName("[GAP] withCollectionSize should constrain emitted collection size")
    void withCollectionSizeShouldConstrainSize() {
        class Container {
            public java.util.List<String> items;
        }
        for (int i = 0; i < 50; i++) {
            Container obj = Mother.forClass(Container.class)
                    .withCollectionSize(20, 30)
                    .build();
            assertTrue(obj.items.size() >= 20 && obj.items.size() <= 30,
                    "got size " + obj.items.size());
        }
    }
}
