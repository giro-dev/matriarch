package dev.agiro.matriarch;

import dev.agiro.matriarch.domain.core.Mother;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionBuilderTest {

    static class Person {
        private String name;
        private int age;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }

    @Test
    void testBuildList() {
        List<Person> people = Mother.forClass(Person.class)
                .buildList(5);

        assertNotNull(people);
        assertEquals(5, people.size());
        people.forEach(person -> {
            assertNotNull(person);
            assertNotNull(person.getName());
        });
    }

    @Test
    void testBuildSet() {
        Set<Person> people = Mother.forClass(Person.class)
                .buildSet(5);

        assertNotNull(people);
        assertEquals(5, people.size());
        people.forEach(person -> {
            assertNotNull(person);
            assertNotNull(person.getName());
        });
    }

    @Test
    void testBuildStreamWithLimit() {
        List<Person> people = Mother.forClass(Person.class)
                .buildStream(5)
                .toList();

        assertNotNull(people);
        assertEquals(5, people.size());
        people.forEach(person -> {
            assertNotNull(person);
            assertNotNull(person.getName());
        });
    }

    @Test
    void testBuildInfiniteStream() {
        List<Person> people = Mother.forClass(Person.class)
                .buildStream()
                .limit(3)
                .toList();

        assertNotNull(people);
        assertEquals(3, people.size());
        people.forEach(person -> {
            assertNotNull(person);
            assertNotNull(person.getName());
        });
    }

    @Test
    void testBuildListWithOverrides() {
        List<Person> people = Mother.forClass(Person.class)
                .forField("age", () -> 25)
                .buildList(3);

        assertNotNull(people);
        assertEquals(3, people.size());
        people.forEach(person -> {
            assertNotNull(person);
            assertEquals(25, person.getAge());
        });
    }

    @Test
    void testForFieldWithSupplier() {
        // Test that forField with Supplier generates fresh values each time
        final int[] counter = {0};
        List<Person> people = Mother.forClass(Person.class)
                .forField("age", () -> counter[0]++)
                .buildList(5);

        assertNotNull(people);
        assertEquals(5, people.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(i, people.get(i).getAge(), "Age should be incremented for each generated object");
        }
    }

    @Test
    void testBuildStreamWithOverrides() {
        List<Person> people = Mother.forClass(Person.class)
                .override("name", "John")
                .buildStream(2)
                .toList();

        assertNotNull(people);
        assertEquals(2, people.size());
        people.forEach(person -> {
            assertNotNull(person);
            assertEquals("John", person.getName());
        });
    }
}

