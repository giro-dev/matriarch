package dev.agiro.matriarch.parametrized_test_source.annotations;

import dev.agiro.matriarch.domain.model.Overrider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify field overrides for test data generation.
 * <p>Supports nested object access using dot notation and regex patterns.
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OverrideField {

    /**
     * Target field to override. Supports nested objects using dot notation.
     * Examples:
     * <ul>
     *   <li>"email" - simple field</li>
     *   <li>"address.city" - nested object field</li>
     *   <li>"user.profile.bio" - deeply nested field</li>
     * </ul>
     */
    String field();

    /**
     * Value to be overridden.
     *
     * <p>If {@code isRegex = true}, this will be treated as a regex pattern
     * and a random value matching the pattern will be generated.
     * Examples:
     * <ul>
     *   <li>"john@example.com" - literal string</li>
     *   <li>"25" - will be converted to appropriate type</li>
     *   <li>"[a-z]{5,10}@gmail.com" - regex pattern (if isRegex = true)</li>
     * </ul>
     */
    String value();

    /**
     * The type of override. Defaults to STRING.
     * <ul>
     *   <li>STRING - treat as string literal</li>
     *   <li>OBJECT - attempt to convert to target field type</li>
     *   <li>REGEX - treat value as regex pattern and generate matching string</li>
     *   <li>NULL - set field to null</li>
     * </ul>
     */
    Overrider.OverriderType type() default Overrider.OverriderType.STRING;

    /**
     * If true, the value will be treated as a regex pattern and a random matching value will be generated.
     * This is a shorthand for setting type = Overrider.OverriderType.REGEX.
     * Example:
     * <pre>{@code
     * @OverrideField(field = "phoneNumber", value = "\\d{3}-\\d{3}-\\d{4}", isRegex = true)
     * }</pre>
     */
    boolean isRegex() default false;

}
