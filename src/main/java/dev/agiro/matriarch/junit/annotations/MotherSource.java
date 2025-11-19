package dev.agiro.matriarch.junit.annotations;

import dev.agiro.matriarch.junit.MotherSourceProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

/**
 * Simpler facade over @MotherFactoryResource for the common case: generate N instances
 * of a single class, with optional overrides/json.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(MotherSourceProvider.class)
public @interface MotherSource {
    Class<?> targetClass();
    int count() default 1;
    OverrideField[] overrides() default {};
    String jsonOverrides() default "";
    String namePattern() default "{index}";
}
