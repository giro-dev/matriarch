package dev.agiro.matriarch.examples;

import dev.agiro.matriarch.junit.annotations.Mother;
import dev.agiro.matriarch.junit.annotations.MotherConfig;
import dev.agiro.matriarch.junit.annotations.MotherSource;
import dev.agiro.matriarch.junit.annotations.OverrideField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Examples showcasing the new @MotherSource and Supplier-based overrides.
 * Note: @MotherConfig automatically enables MotherExtension, no need for @ExtendWith!
 */
@MotherConfig(collectionMin = 2, collectionMax = 5)
public class NewFeaturesDemo {

    // Example 1: Field injection with @Mother
    @Mother(overrides = {@OverrideField(field = "name", value = "John Doe")})
    Person person;

    @Test
    void field_injection_example() {
        assertNotNull(person);
        assertEquals("John Doe", person.name);
    }

    // Example 2: Parameter injection with @Mother
    @Test
    void parameter_injection_example(@Mother Person p) {
        assertNotNull(p);
    }

    // Example 3: Simple @MotherSource for generating multiple test cases
    @ParameterizedTest(name = "Test case {index}")
    @MotherSource(targetClass = Person.class, count = 3)
    void simple_mother_source(Person p) {
        assertNotNull(p);
    }

    // Example 4: @MotherSource with overrides
    @ParameterizedTest
    @MotherSource(
        targetClass = Person.class,
        count = 2,
        overrides = {
            @OverrideField(field = "name", value = "Alice"),
            @OverrideField(field = "age", value = "25")
        }
    )
    void mother_source_with_overrides(Person p) {
        assertEquals("Alice", p.name);
    }

    // Example 5: @MotherSource with regex
    @ParameterizedTest
    @MotherSource(
        targetClass = Person.class,
        count = 2,
        overrides = {
            @OverrideField(field = "email", value = "[a-z]{5,10}@test\\.com", isRegex = true)
        }
    )
    void mother_source_with_regex(Person p) {
        assertTrue(p.email.matches(".*@test\\.com"));
    }

    // Example 6: Supplier-based override (requires NoSupplier marker)
    public static class RandomUuidSupplier implements Supplier<String> {
        @Override
        public String get() {
            return UUID.randomUUID().toString();
        }
    }

    @Test
    void supplier_based_override(
        @Mother(overrides = {
            @OverrideField(field = "id", supplier = RandomUuidSupplier.class)
        }) Person p
    ) {
        assertNotNull(p.id);
        // UUID format check
        assertTrue(p.id.contains("-"));
    }

    // Simple test class
    public static class Person {
        public String id;
        public String name;
        public String email;
        public int age;
    }
}

