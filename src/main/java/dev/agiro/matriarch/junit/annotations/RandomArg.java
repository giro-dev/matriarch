package dev.agiro.matriarch.junit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a single test case argument for parameterized tests using Matriarch's ObjectMother.
 *
 * <p>Each RandomArg generates one test case with the specified target class and optional overrides.
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RandomArg {

    /**
     * The class type to generate for this test case.
     */
    Class<?> targetClass();

    /**
     * Optional name for this test case. If provided, it will appear in test reports
     * making it easier to identify which test case failed.
     * Example: "Valid User", "User with Invalid Email", "Admin User"
     */
    String name() default "";

    /**
     * Field-level overrides using the @OverrideField annotation.
     * This is useful for simple, type-safe overrides.
     * Example:
     * <pre>{@code
     * overrides = {
     *     @OverrideField(field = "email", value = "test@example.com"),
     *     @OverrideField(field = "age", value = "25", type = Overrider.OverriderType.OBJECT)
     * }
     * }</pre>
     */
    OverrideField[] overrides() default {};

    /**
     * JSON-based overrides for more complex scenarios.
     * Supports nested objects and arrays.
     * JSON overrides take precedence over field-level overrides for the same field.
     * Example:
     * <pre>{@code
     * jsonOverrides = """
     *     {
     *         "email": "admin@example.com",
     *         "role": "ADMIN",
     *         "address": {
     *             "city": "New York",
     *             "zipCode": "10001"
     *         }
     *     }
     *     """
     * }</pre>
     */
    String jsonOverrides() default "";
}
