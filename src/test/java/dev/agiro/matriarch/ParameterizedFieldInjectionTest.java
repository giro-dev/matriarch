package dev.agiro.matriarch;

import dev.agiro.matriarch.junit.annotations.Mother;
import dev.agiro.matriarch.junit.annotations.MotherConfig;
import dev.agiro.matriarch.junit.annotations.MotherSource;
import dev.agiro.matriarch.object_samples.NoArgsConstructorBasicObject;
import dev.agiro.matriarch.junit.annotations.OverrideField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.*;

@MotherConfig(collectionMin = 1, collectionMax = 3)
class ParameterizedFieldInjectionTest {

    @Mother(overrides = {
            @OverrideField(field = "string", value = "fixed"),
            @OverrideField(field = "integer", value = "666")
    })
    NoArgsConstructorBasicObject fieldInjected;

    @Test
    void field_is_injected() {
        assertNotNull(fieldInjected);
        assertEquals("fixed", fieldInjected.getString());
        assertEquals(666, fieldInjected.getInteger());
    }

    @Test
    void parameter_is_injected(@Mother(overrides = @OverrideField(field = "integer", value = "666"))
                               NoArgsConstructorBasicObject param) {
        assertNotNull(param);
        assertEquals(666, param.getInteger());

    }

    @ParameterizedTest
    @MotherSource(targetClass = NoArgsConstructorBasicObject.class, count = 2,
            overrides = {@OverrideField(field = "string", value = "abc")})
    void mother_source_works(NoArgsConstructorBasicObject obj) {
        assertNotNull(obj);
        assertEquals("abc", obj.getString());
        assertEquals(666, obj.getKnownInteger());
    }

    @Test
    void test(@Mother(overrides = @OverrideField(field = "string", value = "\\w*", isRegex = true))
              NoArgsConstructorBasicObject obj) {
        assertNotNull(obj);
        System.out.println(obj.getString());
        System.out.println(obj.getInteger());
        System.out.println(obj.getKnownInteger());

    }
}
