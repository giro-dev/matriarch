package dev.agiro.matriarch;

import dev.agiro.matriarch.domain.model.Overrider;
import dev.agiro.matriarch.object_samples.AllArgsConstructorBasicObjectBasicTypes;
import dev.agiro.matriarch.parametrized_test_source.annotations.MotherFactoryResource;
import dev.agiro.matriarch.parametrized_test_source.annotations.OverrideField;
import dev.agiro.matriarch.parametrized_test_source.annotations.RandomArg;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Example test demonstrating the usage of @MotherFactoryResource for parameterized tests.
 * NOTE: This is a demonstration/example file showing how to use the parameterized testing features.
 * Some tests may fail as they are examples and may need adjustment for your specific use case.
 * To use these tests, rename the class to end with "Test" and adjust the test data as needed.
 */
class ParameterizedMotherExample {

    @ParameterizedTest(name = "[{index}] Simple Random Object")
    @MotherFactoryResource(args = {
            @RandomArg(targetClass = AllArgsConstructorBasicObjectBasicTypes.class)
    })
    void testWithSimpleRandomObject(AllArgsConstructorBasicObjectBasicTypes obj) {
        assertNotNull(obj);
        assertNotNull(obj.getString());
        assertNotNull(obj.getInteger());
    }

    @ParameterizedTest(name = "[{index}] {0} - Test with named cases")
    @MotherFactoryResource(args = {
            @RandomArg(
                    name = "Valid Email User",
                    targetClass = AllArgsConstructorBasicObjectBasicTypes.class,
                    overrides = {
                            @OverrideField(field = "string", value = "valid@example.com")
                    }
            ),
            @RandomArg(
                    name = "User with Specific Integer",
                    targetClass = AllArgsConstructorBasicObjectBasicTypes.class,
                    overrides = {
                            @OverrideField(field = "integer", value = "42", type = Overrider.OverriderType.OBJECT)
                    }
            ),
            @RandomArg(
                    name = "User with Regex Email",
                    targetClass = AllArgsConstructorBasicObjectBasicTypes.class,
                    overrides = {
                            @OverrideField(field = "string", value = "[a-z]{5,8}@test.com", isRegex = true)
                    }
            )
    })
    void testWithMultipleNamedCases(AllArgsConstructorBasicObjectBasicTypes obj) {
        assertNotNull(obj);
        assertNotNull(obj.getString());
    }

    @ParameterizedTest(name = "[{index}] Test with JSON overrides")
    @MotherFactoryResource(args = {
            @RandomArg(
                    name = "Complex Object with JSON",
                    targetClass = AllArgsConstructorBasicObjectBasicTypes.class,
                    jsonOverrides = """
                            {
                                "string": "json-value",
                                "integer": 100,
                                "bool": true,
                                "nestedObject": {
                                    "string": "nested-value",
                                    "integer": 200
                                }
                            }
                            """
            )
    })
    void testWithJsonOverrides(AllArgsConstructorBasicObjectBasicTypes obj) {
        assertEquals("json-value", obj.getString());
        assertEquals(100, obj.getInteger());
        assertTrue(obj.isBool());
        assertEquals("nested-value", obj.getNestedObject().getString());
        assertEquals(200, obj.getNestedObject().getInteger());
    }

    @ParameterizedTest(name = "[{index}] Test regex patterns")
    @MotherFactoryResource(args = {
            @RandomArg(
                    name = "Phone Number Pattern",
                    targetClass = AllArgsConstructorBasicObjectBasicTypes.class,
                    overrides = {
                            @OverrideField(field = "string", value = "\\d{3}-\\d{3}-\\d{4}", isRegex = true)
                    }
            ),
            @RandomArg(
                    name = "Email Pattern",
                    targetClass = AllArgsConstructorBasicObjectBasicTypes.class,
                    overrides = {
                            @OverrideField(field = "string", value = "[a-z]{5,10}@(gmail|outlook).com", isRegex = true)
                    }
            )
    })
    void testRegexPatterns(AllArgsConstructorBasicObjectBasicTypes obj) {
        assertNotNull(obj.getString());
        // Verify the string matches some pattern
        assertFalse(obj.getString().isEmpty());
    }

    @ParameterizedTest(name = "[{index}] Mixing overrides")
    @MotherFactoryResource(args = {
            @RandomArg(
                    name = "Mixed Overrides",
                    targetClass = AllArgsConstructorBasicObjectBasicTypes.class,
                    overrides = {
                            @OverrideField(field = "string", value = "from-annotation"),
                            @OverrideField(field = "integer", value = "999", type = Overrider.OverriderType.OBJECT)
                    },
                    jsonOverrides = """
                            {
                                "bool": false,
                                "nestedObject": {
                                    "string": "from-json"
                                }
                            }
                            """
            )
    })
    void testMixedOverrides(AllArgsConstructorBasicObjectBasicTypes obj) {
        assertEquals("from-annotation", obj.getString());
        assertEquals(999, obj.getInteger());
        assertFalse(obj.isBool());
        assertEquals("from-json", obj.getNestedObject().getString());
    }
}

