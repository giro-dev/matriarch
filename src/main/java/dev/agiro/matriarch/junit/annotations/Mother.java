package dev.agiro.matriarch.junit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a field or parameter in a JUnit 5 test to have Matriarch generate an instance automatically.
 *
 * Usage on field:
 *
 * @ExtendWith(MotherExtension.class)
 * class MyTest {
 *     @Mother(overrides = {@OverrideField(field = "email", value = "test@example.com")})
 *     User user;
 * }
 *
 * Usage on parameter:
 *
 * @ExtendWith(MotherExtension.class)
 * void test(@Mother User user) { ... }
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Mother {
    /** Field-level overrides. */
    OverrideField[] overrides() default {};

    /** JSON overrides for complex nested structures. */
    String jsonOverrides() default "";
}

