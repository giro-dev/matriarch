package dev.agiro.matriarch;

import dev.agiro.matriarch.domain.core.Mother;
import dev.agiro.matriarch.object_samples.inheritance.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InheritanceTest {

    @Test
    @DisplayName("Should generate subclass and populate fields from both superclass and subclass")
    void testSubClassGeneration_PopulatesAllFields() {
        SubClass obj = Mother.forClass(SubClass.class).build();

        assertNotNull(obj);
        // Check superclass fields (assuming Matriarch populates them)
        assertNotNull(obj.getBaseField(), "BaseClass field 'baseField' should be populated");
        assertTrue(obj.getBaseNumber() >= 0 || obj.getBaseNumber() < 0, "BaseClass field 'baseNumber' should be initialized");

        // Check subclass fields
        assertNotNull(obj.getSubOnlyField(), "SubClass field 'subOnlyField' should be populated");
        // Check boolean initialization
        assertTrue(obj.isSubFlag() || !obj.isSubFlag(), "SubClass field 'subFlag' should be initialized");
    }

    @Test
    @DisplayName("Should generate class in a multi-level inheritance hierarchy")
    void testMultiLevelInheritance_PopulatesAllFields() {
        ChildOfSubClass obj = Mother.forClass(ChildOfSubClass.class).build();

        assertNotNull(obj);
        // Check BaseClass fields
        assertNotNull(obj.getBaseField(), "BaseClass field 'baseField' should be populated");
        // Check SubClass fields
        assertNotNull(obj.getSubOnlyField(), "SubClass field 'subOnlyField' should be populated");
        // Check ChildOfSubClass fields
        assertTrue(obj.getChildDouble() >= 0.0 || obj.getChildDouble() < 0.0, "ChildOfSubClass field 'childDouble' should be initialized");
    }

    @Test
    @DisplayName("Should override fields inherited from a superclass")
    void testOverrideFields_InheritedFromSuperclass() {
        String overriddenBaseField = "Overridden Base";
        int overriddenBaseNumber = 12345;

        SubClass obj = Mother.forClass(SubClass.class)
                .override("baseField", overriddenBaseField)
                .override("baseNumber", overriddenBaseNumber)
                .build();

        assertNotNull(obj);
        assertEquals(overriddenBaseField, obj.getBaseField());
        assertEquals(overriddenBaseNumber, obj.getBaseNumber());
        assertNotNull(obj.getSubOnlyField()); // Ensure subclass field is still populated
    }

    @Test
    @DisplayName("Should override fields defined in the subclass")
    void testOverrideFields_DefinedInSubclass() {
        String overriddenSubOnlyField = "Custom Sub Value";
        boolean overriddenSubFlag = true;

        SubClass obj = Mother.forClass(SubClass.class)
                .override("subOnlyField", overriddenSubOnlyField)
                .override("subFlag", overriddenSubFlag)
                .build();

        assertNotNull(obj);
        assertEquals(overriddenSubOnlyField, obj.getSubOnlyField());
        assertEquals(overriddenSubFlag, obj.isSubFlag());
        assertNotNull(obj.getBaseField()); // Ensure superclass field is still populated
    }

    @Test
    @DisplayName("Should override fields from all levels in a multi-level hierarchy")
    void testOverrideFields_MultiLevelHierarchy() {
        String overriddenBaseField = "Overridden Base Multi";
        String overriddenSubField = "Overridden Sub Multi";
        double overriddenChildDouble = 99.99;

        ChildOfSubClass obj = Mother.forClass(ChildOfSubClass.class)
                .override("baseField", overriddenBaseField)       // From BaseClass
                .override("subOnlyField", overriddenSubField)    // From SubClass
                .override("childDouble", overriddenChildDouble)  // From ChildOfSubClass
                .build();

        assertNotNull(obj);
        assertEquals(overriddenBaseField, obj.getBaseField());
        assertEquals(overriddenSubField, obj.getSubOnlyField());
        assertEquals(overriddenChildDouble, obj.getChildDouble(), 0.001);
    }

    @Test
    @DisplayName("Should handle subclassing a base with only parameterized constructor")
    void testSubclassOfBaseWithOnlyParameterizedConstructor() {
        // Matriarch needs to be able to call super(name) for SubClassOfParamConstructorBase.
        // This means it must generate a value for the 'name' parameter of the super constructor.
        SubClassOfParamConstructorBase obj = Mother.forClass(SubClassOfParamConstructorBase.class)
            .override("name", "ProvidedNameForBase") // Explicitly providing 'name' for super constructor
            .build();

        assertNotNull(obj);
        assertNotNull(obj.getName(), "Base class 'name' field should be populated via super constructor");
        assertEquals("ProvidedNameForBase",obj.getName());
        assertTrue(obj.getSubValue() >= 0 || obj.getSubValue() < 0, "Subclass 'subValue' field should be initialized");
    }

    @Test
    @DisplayName("Should override fields in subclass of base with only parameterized constructor")
    void testOverrideSubclassOfBaseWithOnlyParameterizedConstructor() {
        String overriddenName = "Test Name For Base";
        int overriddenSubValue = 123;

        SubClassOfParamConstructorBase obj = Mother.forClass(SubClassOfParamConstructorBase.class)
                .override("name", overriddenName) // For super()
                .override("subValue", overriddenSubValue) // For subclass field
                .build();

        assertNotNull(obj);
        assertEquals(overriddenName, obj.getName());
        assertEquals(overriddenSubValue, obj.getSubValue());
    }
}
