package dev.agiro.matriarch;

import dev.agiro.matriarch.domain.core.Mother;
import dev.agiro.matriarch.domain.model.TypeReference;
import dev.agiro.matriarch.object_samples.edgecases.*;
import dev.agiro.matriarch.object_samples.edgecases.sealedhierarchy.SealedBase;
import dev.agiro.matriarch.object_samples.edgecases.sealedhierarchy.SealedInterface;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import static org.junit.jupiter.api.Assertions.*;

class InstantiationPatternsTest {

    @Test
    @DisplayName("Should instantiate a class with no fields")
    void testClassWithoutFields() {
        ClassWithoutFields obj = Mother.forClass(ClassWithoutFields.class).build();
        assertNotNull(obj);
        assertEquals("ok", obj.doSomething());
    }

    @Test
    @DisplayName("Should instantiate a class with all final fields")
    void testClassWithAllFinalFields() {
        ClassWithAllFinalFields obj = Mother.forClass(ClassWithAllFinalFields.class).build();
        assertNotNull(obj);
        assertNotNull(obj.getName());
        assertTrue(obj.getCode() >= 0);
    }

    @Test
    @DisplayName("Should override a class with all final fields")
    void testClassWithAllFinalFieldsOverride() {
        ClassWithAllFinalFields obj = Mother.forClass(ClassWithAllFinalFields.class)
                .forField("name", "x")
                .forField("code", 123)
                .build();
        assertEquals("x", obj.getName());
        assertEquals(123, obj.getCode());
    }

    @Test
    @DisplayName("Should instantiate a final class with all final fields")
    void testFinalClassWithAllFinalFields() {
        FinalClassWithAllFinalFields obj = Mother.forClass(FinalClassWithAllFinalFields.class).build();
        assertNotNull(obj);
        assertNotNull(obj.getLabel());
    }

    @Test
    @DisplayName("Should override a final class with all final fields")
    void testFinalClassWithAllFinalFieldsOverride() {
        FinalClassWithAllFinalFields obj = Mother.forClass(FinalClassWithAllFinalFields.class)
                .forField("label", "x")
                .forField("amount", 999L)
                .build();
        assertEquals("x", obj.getLabel());
        assertEquals(999L, obj.getAmount());
    }

    @Test
    @DisplayName("Should instantiate a class where the constructor hides the property name")
    void testClassWithHiddenProperty() {
        ClassWithHiddenProperty obj = Mother.forClass(ClassWithHiddenProperty.class).build();
        assertNotNull(obj);
        assertNotNull(obj.getMaskedValue());
        assertTrue(obj.getMaskedValue().startsWith("masked-"));
    }

    @Test
    @DisplayName("Should override the constructor input of a class with a hidden property")
    void testClassWithHiddenPropertyOverride() {
        ClassWithHiddenProperty obj = Mother.forClass(ClassWithHiddenProperty.class)
                .forField("input", "test")
                .build();
        assertEquals("masked-" + "test".hashCode(), obj.getMaskedValue());
    }

    @Test
    @DisplayName("Should instantiate a class whose constructor parameter name differs from the field")
    void testClassWithConstructorParameterMismatch() {
        ClassWithConstructorParameterMismatch obj = Mother.forClass(ClassWithConstructorParameterMismatch.class).build();
        assertNotNull(obj);
        assertNotNull(obj.getValue());
        assertEquals(obj.getValue(), obj.getValue().toUpperCase());
    }

    @Test
    @DisplayName("Should override the constructor parameter that differs from the field")
    void testClassWithConstructorParameterMismatchOverride() {
        ClassWithConstructorParameterMismatch obj = Mother.forClass(ClassWithConstructorParameterMismatch.class)
                .forField("data", "hello")
                .build();
        assertEquals("HELLO", obj.getValue());
    }

    @Test
    @DisplayName("Should choose the constructor with fewer parameters")
    void testClassWithOverloadedConstructors() {
        ClassWithOverloadedConstructors obj = Mother.forClass(ClassWithOverloadedConstructors.class).build();
        assertNotNull(obj);
        assertNotNull(obj.getName());
        assertTrue(obj.getCode() >= 0);
    }

    @Test
    @DisplayName("Should override fields in a class with overloaded constructors")
    void testClassWithOverloadedConstructorsOverride() {
        ClassWithOverloadedConstructors obj = Mother.forClass(ClassWithOverloadedConstructors.class)
                .forField("name", "x")
                .forField("code", 7)
                .build();
        assertEquals("x", obj.getName());
        assertEquals(7, obj.getCode());
    }

    @Test
    @DisplayName("Should instantiate a class with a package-private constructor")
    void testClassWithPackagePrivateConstructor() {
        ClassWithPackagePrivateConstructor obj = Mother.forClass(ClassWithPackagePrivateConstructor.class).build();
        assertNotNull(obj);
        assertNotNull(obj.getLabel());
        assertTrue(obj.getCount() >= 0);
    }

    @Test
    @DisplayName("Should override fields in a class with a package-private constructor")
    void testClassWithPackagePrivateConstructorOverride() {
        ClassWithPackagePrivateConstructor obj = Mother.forClass(ClassWithPackagePrivateConstructor.class)
                .forField("label", "x")
                .forField("count", 5)
                .build();
        assertEquals("x", obj.getLabel());
        assertEquals(5, obj.getCount());
    }

    @Test
    @DisplayName("Should instantiate a class with a protected constructor")
    void testClassWithProtectedConstructor() {
        ClassWithProtectedConstructor obj = Mother.forClass(ClassWithProtectedConstructor.class).build();
        assertNotNull(obj);
        assertNotNull(obj.getLabel());
        assertTrue(obj.getCount() >= 0);
    }

    @Test
    @DisplayName("Should override fields in a class with a protected constructor")
    void testClassWithProtectedConstructorOverride() {
        ClassWithProtectedConstructor obj = Mother.forClass(ClassWithProtectedConstructor.class)
                .forField("label", "x")
                .forField("count", 5)
                .build();
        assertEquals("x", obj.getLabel());
        assertEquals(5, obj.getCount());
    }

    @Test
    @DisplayName("Should instantiate a class with a private constructor via a public static factory")
    void testClassWithPrivateConstructorAndPublicStaticFactory() {
        ClassWithPrivateConstructorAndPublicStaticFactory obj = Mother.forClass(ClassWithPrivateConstructorAndPublicStaticFactory.class).build();
        assertNotNull(obj);
        assertEquals("factory", obj.getName());
        assertEquals(1, obj.getVersion());
    }

    @Test
    @DisplayName("Should override fields in a class created by a public static factory")
    void testClassWithPrivateConstructorAndPublicStaticFactoryOverride() {
        ClassWithPrivateConstructorAndPublicStaticFactory obj = Mother.forClass(ClassWithPrivateConstructorAndPublicStaticFactory.class)
                .forField("name", "x")
                .forField("version", 99)
                .build();
        assertEquals("x", obj.getName());
        assertEquals(99, obj.getVersion());
    }

    @Test
    @DisplayName("Should fail for a class with a private constructor and a parameterized static factory")
    void testClassWithPrivateConstructorAndParamStaticFactory() {
        assertThrows(Exception.class, () -> Mother.forClass(ClassWithPrivateConstructorAndParamStaticFactory.class).build());
    }

    @Test
    @DisplayName("Should fail for a class with a private constructor and no static factory")
    void testClassWithPrivateConstructorAndNoStaticFactory() {
        assertThrows(Exception.class, () -> Mother.forClass(ClassWithPrivateConstructorAndNoStaticFactory.class).build());
    }

    @Test
    @DisplayName("Should fail for a class with a builder pattern instead of a constructor")
    void testClassWithBuilderPattern() {
        assertThrows(Exception.class, () -> Mother.forClass(ClassWithBuilderPattern.class).build());
    }

    @Test
    @DisplayName("Should select the no-arg static factory when multiple factories exist")
    void testClassWithMultipleStaticFactories() {
        ClassWithMultipleStaticFactories obj = Mother.forClass(ClassWithMultipleStaticFactories.class).build();
        assertNotNull(obj);
        assertEquals("default", obj.getConfigName());
        assertEquals(0, obj.getVersion());
    }

    @Test
    @DisplayName("Should override fields when using the no-arg static factory")
    void testClassWithMultipleStaticFactoriesOverride() {
        ClassWithMultipleStaticFactories obj = Mother.forClass(ClassWithMultipleStaticFactories.class)
                .forField("configName", "x")
                .forField("version", 5)
                .build();
        assertEquals("x", obj.getConfigName());
        assertEquals(5, obj.getVersion());
    }

    @Test
    @DisplayName("Should prefer a no-arg static factory over the default constructor")
    void testClassWithStaticFactoryAndDefaultConstructor() {
        ClassWithStaticFactoryAndDefaultConstructor obj = Mother.forClass(ClassWithStaticFactoryAndDefaultConstructor.class).build();
        assertNotNull(obj);
        assertEquals("factory", obj.getName());
        assertEquals(42, obj.getValue());
    }

    @Test
    @DisplayName("Should override fields created by a no-arg static factory")
    void testClassWithStaticFactoryAndDefaultConstructorOverride() {
        ClassWithStaticFactoryAndDefaultConstructor obj = Mother.forClass(ClassWithStaticFactoryAndDefaultConstructor.class)
                .forField("name", "x")
                .forField("value", 7)
                .build();
        assertEquals("x", obj.getName());
        assertEquals(7, obj.getValue());
    }

    @Test
    @DisplayName("Should fall back to the constructor when the static factory fails")
    void testClassWithStaticFactoryThatThrows() {
        ClassWithStaticFactoryThatThrows obj = Mother.forClass(ClassWithStaticFactoryThatThrows.class).build();
        assertNotNull(obj);
        assertEquals("constructor", obj.getOrigin());
    }

    @Test
    @DisplayName("Should use the public constructor when the static factory is private")
    void testClassWithPublicConstructorAndHiddenStaticFactory() {
        ClassWithPublicConstructorAndHiddenStaticFactory obj = Mother.forClass(ClassWithPublicConstructorAndHiddenStaticFactory.class).build();
        assertEquals("constructor", obj.getSource());
    }

    @Test
    @DisplayName("Should use the public constructor when the static factory is package-private")
    void testClassWithPackagePrivateStaticFactory() {
        ClassWithPackagePrivateStaticFactory obj = Mother.forClass(ClassWithPackagePrivateStaticFactory.class).build();
        assertEquals("constructor", obj.getSource());
    }

    @Test
    @DisplayName("Should instantiate a class using a static factory that returns a subclass")
    void testClassWithStaticFactoryReturningSubclass() {
        ClassWithStaticFactoryReturningSubclass obj = Mother.forClass(ClassWithStaticFactoryReturningSubclass.class).build();
        assertNotNull(obj);
        assertEquals("subclass", obj.getName());
    }

    @Test
    @DisplayName("Should override fields in an object created by a static factory returning a subclass")
    void testClassWithStaticFactoryReturningSubclassOverride() {
        ClassWithStaticFactoryReturningSubclass obj = Mother.forClass(ClassWithStaticFactoryReturningSubclass.class)
                .forField("name", "x")
                .build();
        assertEquals("x", obj.getName());
    }

    @Test
    @DisplayName("Should instantiate an abstract class using a static factory")
    void testAbstractClassWithStaticFactory() {
        AbstractClassWithStaticFactory obj = Mother.forClass(AbstractClassWithStaticFactory.class).build();
        assertNotNull(obj);
        assertEquals("abstract-factory", obj.getName());
    }

    @Test
    @DisplayName("Should instantiate a class using a static factory that returns a cached singleton")
    void testClassWithStaticFactoryReturningCachedInstance() {
        ClassWithStaticFactoryReturningCachedInstance obj = Mother.forClass(ClassWithStaticFactoryReturningCachedInstance.class).build();
        assertNotNull(obj);
        assertEquals("cached", obj.getName());
    }

    @Test
    @DisplayName("Should instantiate a sealed class using a static factory")
    void testSealedClass() {
        SealedBase obj = Mother.forClass(SealedBase.class).build();
        assertNotNull(obj);
        assertEquals("factory", obj.getType());
        assertEquals(42, obj.getValue());
    }

    @Test
    @DisplayName("Should override fields in a sealed class")
    void testSealedClassOverride() {
        SealedBase obj = Mother.forClass(SealedBase.class)
                .forField("type", "x")
                .forField("value", 7)
                .build();
        assertEquals("x", obj.getType());
        assertEquals(7, obj.getValue());
    }

    @Test
    @DisplayName("Should instantiate a sealed interface using a static factory")
    void testSealedInterface() {
        SealedInterface obj = Mother.forClass(SealedInterface.class).build();
        assertNotNull(obj);
        assertEquals("factory", obj.label());
    }

    @Test
    @DisplayName("Should instantiate a record with all final components")
    void testRecordWithAllComponentsFinal() {
        RecordWithAllComponentsFinal record = Mother.forClass(RecordWithAllComponentsFinal.class).build();
        assertNotNull(record);
        assertNotNull(record.name());
        assertTrue(record.code() >= 0);
    }

    @Test
    @DisplayName("Should override a record with all final components")
    void testRecordWithAllComponentsFinalOverride() {
        RecordWithAllComponentsFinal record = Mother.forClass(RecordWithAllComponentsFinal.class)
                .forField("name", "x")
                .forField("code", 5)
                .build();
        assertEquals("x", record.name());
        assertEquals(5, record.code());
    }

    @Test
    @DisplayName("Should instantiate a record with a static factory")
    void testRecordWithStaticFactory() {
        RecordWithStaticFactory record = Mother.forClass(RecordWithStaticFactory.class).build();
        assertNotNull(record);
        assertEquals("factory", record.name());
        assertEquals(100, record.score());
    }

    @Test
    @DisplayName("Should instantiate a record with compact constructor transformations")
    void testRecordWithHiddenComputation() {
        RecordWithHiddenComputation record = Mother.forClass(RecordWithHiddenComputation.class)
                .forField("raw", " hello ")
                .forField("value", 5)
                .build();
        assertEquals(" HELLO ", record.raw());
        assertEquals(10, record.value());
    }

    @Test
    @DisplayName("Should instantiate a record with validation")
    void testRecordWithValidation() {
        RecordWithValidation record = Mother.forClass(RecordWithValidation.class).build();
        assertNotNull(record);
        assertNotNull(record.name());
        assertTrue(record.score() >= 0);
    }

    @Test
    @DisplayName("Should fail when a record validation rejects a value")
    void testRecordWithValidationRejectsBlankName() {
        assertThrows(Exception.class, () -> Mother.forClass(RecordWithValidation.class)
                .forField("name", "   ")
                .forField("score", 5)
                .build());
    }

    @Test
    @DisplayName("Should instantiate a record with many components")
    void testRecordWithManyComponents() {
        RecordWithManyComponents record = Mother.forClass(RecordWithManyComponents.class).build();
        assertNotNull(record);
        assertNotNull(record.a());
        assertNotNull(record.e());
        assertNotNull(record.f());
        assertFalse(record.f().isEmpty());
    }

    @Test
    @DisplayName("Should instantiate a class with final and non-final fields")
    void testClassWithMixedFinalAndNonFinalFields() {
        ClassWithMixedFinalAndNonFinalFields obj = Mother.forClass(ClassWithMixedFinalAndNonFinalFields.class).build();
        assertNotNull(obj);
        assertTrue(obj.getCode() >= 0);
        assertNotNull(obj.getName());
    }

    @Test
    @DisplayName("Should override final and non-final fields")
    void testClassWithMixedFinalAndNonFinalFieldsOverride() {
        ClassWithMixedFinalAndNonFinalFields obj = Mother.forClass(ClassWithMixedFinalAndNonFinalFields.class)
                .forField("code", 42)
                .forField("name", "mixed")
                .build();
        assertEquals(42, obj.getCode());
        assertEquals("mixed", obj.getName());
    }

    @Test
    @DisplayName("Should instantiate a class with only a final-field no-arg constructor")
    void testClassWithNoArgsConstructorAndOnlyFinalFields() {
        ClassWithNoArgsConstructorAndOnlyFinalFields obj = Mother.forClass(ClassWithNoArgsConstructorAndOnlyFinalFields.class).build();
        assertNotNull(obj);
        assertEquals("default", obj.getName());
        assertEquals(0, obj.getCount());
    }

    @Test
    @DisplayName("Should instantiate a class with only an all-args constructor")
    void testClassWithAllArgsConstructorAndNoDefault() {
        ClassWithAllArgsConstructorAndNoDefault obj = Mother.forClass(ClassWithAllArgsConstructorAndNoDefault.class).build();
        assertNotNull(obj);
        assertNotNull(obj.getName());
        assertTrue(obj.getCount() >= 0);
    }

    @Test
    @DisplayName("Should override a class with only an all-args constructor")
    void testClassWithAllArgsConstructorAndNoDefaultOverride() {
        ClassWithAllArgsConstructorAndNoDefault obj = Mother.forClass(ClassWithAllArgsConstructorAndNoDefault.class)
                .forField("name", "x")
                .forField("count", 5)
                .build();
        assertEquals("x", obj.getName());
        assertEquals(5, obj.getCount());
    }

    @Test
    @DisplayName("Should instantiate a class with a default constructor and setters")
    void testClassWithDefaultConstructorAndSetters() {
        ClassWithDefaultConstructorAndSetters obj = Mother.forClass(ClassWithDefaultConstructorAndSetters.class).build();
        assertNotNull(obj);
        assertNotNull(obj.getName());
        assertTrue(obj.getCount() >= 0);
    }

    @Test
    @DisplayName("Should override a class with a default constructor and setters")
    void testClassWithDefaultConstructorAndSettersOverride() {
        ClassWithDefaultConstructorAndSetters obj = Mother.forClass(ClassWithDefaultConstructorAndSetters.class)
                .forField("name", "x")
                .forField("count", 5)
                .build();
        assertEquals("x", obj.getName());
        assertEquals(5, obj.getCount());
    }

    @Test
    @DisplayName("Should instantiate a class whose constructor throws on null only when null is provided")
    void testClassWithConstructorThatThrowsOnNull() {
        ClassWithConstructorThatThrowsOnNull obj = Mother.forClass(ClassWithConstructorThatThrowsOnNull.class).build();
        assertNotNull(obj);
        assertNotNull(obj.getLabel());
    }

    @Test
    @DisplayName("Should fail when constructor rejects a null value")
    void testClassWithConstructorThatThrowsOnNullOverride() {
        assertThrows(Exception.class, () -> Mother.forClass(ClassWithConstructorThatThrowsOnNull.class)
                .forField("label", (String) null)
                .build());
    }

    @Test
    @DisplayName("Should instantiate a class with varargs constructor using overrides")
    void testClassWithVarargsConstructor() {
        ClassWithVarargsConstructor obj = Mother.forClass(ClassWithVarargsConstructor.class)
                .forField("name", "test")
                .forField("tags", new String[]{"a", "b"})
                .build();
        assertEquals("test", obj.getName());
        assertArrayEquals(new String[]{"a", "b"}, obj.getTags());
        assertEquals(2, obj.tagCount());
    }

    @Test
    @DisplayName("Should instantiate a class with Optional fields")
    void testClassWithOptionalFields() {
        ClassWithOptionalFields obj = Mother.forClass(ClassWithOptionalFields.class).build();
        assertNotNull(obj);
        assertNotNull(obj.getOptionalString());
        assertTrue(obj.getOptionalString().isEmpty());
        assertTrue(obj.getOptionalInt().isEmpty());
        assertTrue(obj.getOptionalLong().isEmpty());
        assertTrue(obj.getOptionalDouble().isEmpty());
    }

    @Test
    @DisplayName("Should override Optional fields")
    void testClassWithOptionalFieldsOverride() {
        ClassWithOptionalFields obj = Mother.forClass(ClassWithOptionalFields.class)
                .forField("optionalString", Optional.of("hello"))
                .forField("optionalInt", OptionalInt.of(1))
                .forField("optionalLong", OptionalLong.of(2L))
                .forField("optionalDouble", OptionalDouble.of(3.0))
                .build();
        assertEquals(Optional.of("hello"), obj.getOptionalString());
        assertEquals(OptionalInt.of(1), obj.getOptionalInt());
        assertEquals(OptionalLong.of(2L), obj.getOptionalLong());
        assertEquals(OptionalDouble.of(3.0), obj.getOptionalDouble());
    }

    @Test
    @DisplayName("Should instantiate a class with nested static class fields")
    void testClassWithNestedClassFields() {
        ClassWithNestedClassFields obj = Mother.forClass(ClassWithNestedClassFields.class).build();
        assertNotNull(obj);
        assertNotNull(obj.getNested());
        assertNotNull(obj.getNested().getValue());
    }

    @Test
    @DisplayName("Should instantiate a class with enum fields")
    void testClassWithEnumField() {
        ClassWithEnumField obj = Mother.forClass(ClassWithEnumField.class).build();
        assertNotNull(obj);
        assertNotNull(obj.getStatus());
    }

    @Test
    @DisplayName("Should instantiate a class with generic fields")
    void testClassWithGenericField() {
        ClassWithGenericField obj = Mother.forClass(ClassWithGenericField.class).build();
        assertNotNull(obj);
        assertNotNull(obj.getGeneric());
        assertTrue(obj.getGeneric().getValue() instanceof String);
    }

    @Test
    @DisplayName("Should instantiate a generic class using TypeReference")
    void testGenericClassWithTypeReference() {
        ClassWithGeneric<String> obj = Mother.forType(new TypeReference<ClassWithGeneric<String>>() {})
                .forField("value", "generic")
                .build();
        assertNotNull(obj);
        assertEquals("generic", obj.getValue());
    }

    @Test
    @DisplayName("Should instantiate a class with collection fields")
    void testClassWithCollectionFields() {
        ClassWithCollectionFields obj = Mother.forClass(ClassWithCollectionFields.class).build();
        assertNotNull(obj);
        assertNotNull(obj.getItems());
        assertFalse(obj.getItems().isEmpty());
        assertNotNull(obj.getNumbers());
        assertFalse(obj.getNumbers().isEmpty());
        assertNotNull(obj.getMappings());
        assertFalse(obj.getMappings().isEmpty());
    }
}
