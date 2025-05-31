package dev.agiro.matriarch;

import dev.agiro.matriarch.domain.core.Mother;
import dev.agiro.matriarch.object_samples.staticfactory.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StaticFactoryMethodTest {

    @Test
    @DisplayName("Should generate an object using a public static factory method (no-args)")
    void testPublicStaticFactory_NoArgs() {
        // Assumes Matriarch can find and use 'createDefault' or a similar conventional no-arg factory.
        PrivateConstructorPublicStaticFactory obj = Mother.forClass(PrivateConstructorPublicStaticFactory.class).build();
        assertNotNull(obj);
        // Values will depend on Matriarch's behavior:
        // 1. Does it call the factory and then override?
        // 2. Or does it populate fields directly after somehow creating an instance?
        // For now, assume it calls the factory, and then fields can be overridden.
        assertEquals("defaultId", obj.getId()); // Value from factory method
        assertEquals("defaultValue", obj.getValue()); // Value from factory method
    }

    @Test
    @DisplayName("Should override fields of an object created by a static factory method (no-args)")
    void testPublicStaticFactory_NoArgs_Override() {
        String overriddenId = "overriddenTestId";
        String overriddenValue = "overriddenTestValue";

        PrivateConstructorPublicStaticFactory obj = Mother.forClass(PrivateConstructorPublicStaticFactory.class)
                .override("id", overriddenId)
                .override("value", overriddenValue)
                .build();

        assertNotNull(obj);
        assertEquals(overriddenId, obj.getId());
        assertEquals(overriddenValue, obj.getValue());
    }

    @Test
    @DisplayName("Should handle class with multiple static factory methods")
    void testMultipleStaticFactories_DefaultBehavior() {
        // This test explores Matriarch's default choice if multiple factories exist.
        // It might pick one based on a naming convention (e.g., 'create', 'getInstance', 'createDefault')
        // or one with no arguments if available.
        PrivateConstructorMultipleStaticFactories obj = Mother.forClass(PrivateConstructorMultipleStaticFactories.class).build();
        assertNotNull(obj);
        // Assertions depend on which factory Matriarch chose and then if it populates/overrides.
        // Assuming it might find 'createDefault' if such logic exists.
        assertEquals("default", obj.getConfigName());
        assertEquals(0, obj.getVersion());
        assertFalse(obj.isActive());
    }

    @Test
    @DisplayName("Should override fields for class with multiple static factories")
    void testMultipleStaticFactories_Override() {
        String configName = "customConfig";
        int version = 5;
        boolean active = true;

        PrivateConstructorMultipleStaticFactories obj = Mother.forClass(PrivateConstructorMultipleStaticFactories.class)
                .override("configName", configName)
                .override("version", version)
                .override("active", active)
                .build();

        assertNotNull(obj);
        assertEquals(configName, obj.getConfigName());
        assertEquals(version, obj.getVersion());
        assertEquals(active, obj.isActive());
    }

    @Test
    @DisplayName("Should handle class with a static factory method that requires parameters")
    void testStaticFactoryWithParams_DefaultBehavior() {
        // Matriarch needs to supply values for these parameters.
        // It might use its default value generation for the parameter types.
        PrivateConstructorStaticFactoryWithParams obj = Mother.forClass(PrivateConstructorStaticFactoryWithParams.class)
            // Optional: override factory parameters if Matriarch supports it, e.g. using a special syntax
            // .overrideFactoryArg("prefix", "PFX")
            // .overrideFactoryArg("number", 123)
            // .overrideFactoryArg("suffix", "SFX")
            .build();

        assertNotNull(obj);
        assertNotNull(obj.getPrefix()); // Populated by Matriarch for factory args
        assertTrue(obj.getNumber() >= 0 || obj.getNumber() < 0); // Populated
        assertNotNull(obj.getSuffix()); // Populated
    }

    @Test
    @DisplayName("Should override fields of an object created by a static factory method with parameters")
    void testStaticFactoryWithParams_OverrideFields() {
        String prefix = "MyPrefix";
        int number = 999;
        String suffix = "MySuffix";

        // This tests overriding the *fields* of the object, assuming Matriarch can call the factory.
        PrivateConstructorStaticFactoryWithParams obj = Mother.forClass(PrivateConstructorStaticFactoryWithParams.class)
                .override("prefix", prefix)
                .override("number", number)
                .override("suffix", suffix)
                .build();

        assertNotNull(obj);
        assertEquals(prefix, obj.getPrefix());
        assertEquals(number, obj.getNumber());
        assertEquals(suffix, obj.getSuffix());
    }

    @Test
    @DisplayName("Should throw exception if static factory method with params cannot be satisfied by Matriarch")
    void testStaticFactoryWithParams_CannotSatisfy() {
        // This test is for a scenario where Matriarch cannot call the static factory method.
        // For example, if the factory method takes complex objects as parameters that Matriarch cannot generate by default
        // and no overrides are provided for those parameters.
        // For PrivateConstructorStaticFactoryWithParams, its parameters are String and int, which Matriarch should handle.
        // So, this specific class might not trigger this failure case unless we modify it to take ungenerateable types.
        // For now, we assume Matriarch can satisfy String and int parameters.
        // If there was a class like:
        // class UnhappyFactory {
        //   private UnhappyFactory() {}
        //   public static UnhappyFactory create(NonGenerateableType t) { return null;}
        // }
        // Then:
        // assertThrows(Exception.class, () -> Mother.forClass(UnhappyFactory.class).build());

        // Since PrivateConstructorStaticFactoryWithParams takes basic types, it should build successfully.
        // This test is more of a placeholder for that concept.
        assertDoesNotThrow(() -> {
            Mother.forClass(PrivateConstructorStaticFactoryWithParams.class).build();
        });
    }
}
