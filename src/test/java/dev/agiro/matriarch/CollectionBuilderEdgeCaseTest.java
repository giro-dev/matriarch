package dev.agiro.matriarch;

import dev.agiro.matriarch.domain.core.Mother;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Edge cases for the collection builder API (buildList/Set/Stream,
 * withCollectionSize validation).
 */
class CollectionBuilderEdgeCaseTest {

    public static class Person {
        private String name;
        private int age;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }

    @Test
    @DisplayName("buildList(0) returns an empty list")
    void buildListZero() {
        List<Person> list = Mother.forClass(Person.class).buildList(0);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("buildSet(0) returns an empty set")
    void buildSetZero() {
        Set<Person> set = Mother.forClass(Person.class).buildSet(0);
        assertNotNull(set);
        assertTrue(set.isEmpty());
    }

    @Test
    @DisplayName("buildStream(0) returns an empty stream")
    void buildStreamZero() {
        Stream<Person> stream = Mother.forClass(Person.class).buildStream(0);
        assertNotNull(stream);
        assertEquals(0, stream.count());
    }

    @Test
    @DisplayName("buildList with negative count throws IllegalArgumentException")
    void buildListNegative() {
        var builder = Mother.forClass(Person.class);
        assertThrows(IllegalArgumentException.class, () -> builder.buildList(-1));
    }

    @Test
    @DisplayName("buildSet with negative count throws IllegalArgumentException")
    void buildSetNegative() {
        var builder = Mother.forClass(Person.class);
        assertThrows(IllegalArgumentException.class, () -> builder.buildSet(-5));
    }

    @Test
    @DisplayName("buildStream with negative count throws IllegalArgumentException")
    void buildStreamNegative() {
        var builder = Mother.forClass(Person.class);
        assertThrows(IllegalArgumentException.class, () -> builder.buildStream(-100));
    }

    @Test
    @DisplayName("withCollectionSize rejects negative min")
    void withCollectionSizeNegativeMin() {
        var builder = Mother.forClass(Person.class);
        assertThrows(IllegalArgumentException.class, () -> builder.withCollectionSize(-1, 5));
    }

    @Test
    @DisplayName("withCollectionSize rejects inverted ranges (max < min)")
    void withCollectionSizeInverted() {
        var builder = Mother.forClass(Person.class);
        assertThrows(IllegalArgumentException.class, () -> builder.withCollectionSize(10, 3));
    }

    @Test
    @DisplayName("withCollectionSize(single) rejects a negative size")
    void withCollectionSizeSingleNegative() {
        var builder = Mother.forClass(Person.class);
        assertThrows(IllegalArgumentException.class, () -> builder.withCollectionSize(-2));
    }

    @Test
    @DisplayName("withCollectionSize(single) sets both bounds equal")
    void withCollectionSizeSingleSetsBothBounds() {
        var builder = Mother.forClass(Person.class).withCollectionSize(7);
        assertEquals(7, builder.getCollectionSizeMin());
        assertEquals(7, builder.getCollectionSizeMax());
    }

    @Test
    @DisplayName("buildList produces distinct Person instances (not the same reference)")
    void buildListProducesDistinctInstances() {
        List<Person> list = Mother.forClass(Person.class).buildList(10);
        for (int i = 1; i < list.size(); i++) {
            assertNotSame(list.get(0), list.get(i),
                    "Each generated object should be a fresh instance");
        }
    }

    @Test
    @DisplayName("buildStream() produces an infinite stream (limit works)")
    void infiniteStream() {
        long count = Mother.forClass(Person.class).buildStream().limit(25).count();
        assertEquals(25, count);
    }

    @Test
    @DisplayName("buildList preserves per-field supplier state across calls")
    void buildListPerFieldSupplierCounter() {
        final int[] counter = {0};
        List<Person> list = Mother.forClass(Person.class)
                .forField("age", () -> ++counter[0])
                .buildList(4);
        assertEquals(List.of(1, 2, 3, 4), list.stream().map(Person::getAge).toList());
    }
}
