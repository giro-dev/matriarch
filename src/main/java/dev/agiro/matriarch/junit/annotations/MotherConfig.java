package dev.agiro.matriarch.junit.annotations;

import dev.agiro.matriarch.junit.MotherExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class-level configuration for Mother-based generation in tests.
 * This annotation automatically enables the MotherExtension, so you don't need @ExtendWith.
 *
 * <p>Example usage:
 * <pre>{@code
 * @MotherConfig(collectionMin = 2, collectionMax = 5)
 * class MyTest {
 *     @Mother User user;
 *
 *     @Test
 *     void test() {
 *         assertNotNull(user);
 *     }
 * }
 * }</pre>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(MotherExtension.class)
public @interface MotherConfig {
    /** Optional seed for deterministic runs (reserved for future use). */
    long seed() default -1L;

    /** Default min collection size. */
    int collectionMin() default 1;

    /** Default max collection size. */
    int collectionMax() default 15;

    /** If true, jsonOverrides take precedence over array overrides; otherwise the opposite. */
    boolean jsonOverridesWin() default true;

    /**
     * Optional path to a custom known patterns YAML file.
     * If not specified, uses the default patterns.yaml from src/test/resources.
     * Can be:
     * - A classpath resource: "classpath:my-patterns.yaml"
     * - A file path: "file:/path/to/patterns.yaml"
     * - Just a filename: "patterns.yaml" (searches classpath)
     */
    String knownPatterns() default "";
}

