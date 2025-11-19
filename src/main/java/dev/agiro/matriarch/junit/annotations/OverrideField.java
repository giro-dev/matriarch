package dev.agiro.matriarch.junit.annotations;

import dev.agiro.matriarch.domain.model.Overrider;
import dev.agiro.matriarch.junit.annotations.internal.NoSupplier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

/**
 * Annotation to specify field overrides for test data generation.
 * <p>Supports nested object access using dot notation and regex patterns.</p>
 * <p>Simplified precedence:</p>
 * <ol>
 *   <li>If {@code supplier()} is set (not {@code NoSupplier}), use it.</li>
 *   <li>Else if {@code isRegex = true}, treat {@code value()} as regex.</li>
 *   <li>Else use {@code type()} with {@code value()}.</li>
 * </ol>
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
     * Optional value to be overridden. Defaults to empty when using {@code supplier()}.
     *
     * <p>If {@code isRegex = true}, this will be treated as a regex pattern
     * and a random value matching the pattern will be generated.</p>
     */
    String value() default "";

    /**
     * Optional Supplier class to compute values programmatically.
     * Must have a public no-arg constructor.
     */
    Class<? extends Supplier<?>> supplier() default NoSupplier.class;

    /**
     * The type of override. Defaults to STRING. Ignored if {@code isRegex} is true or {@code supplier()} is set.
     */
    Overrider.OverriderType type() default Overrider.OverriderType.STRING;

    /**
     * If true, the value will be treated as a regex pattern and a random matching value will be generated.
     * Shorthand for setting type = REGEX. Ignored when {@code supplier()} is set.
     */
    boolean isRegex() default false;

}
