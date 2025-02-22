package dev.agiro.matriarch.parametrized_test_source.annotations;

import dev.agiro.matriarch.domain.model.Overrider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OverrideField {

    /**
     * target field to override. nested objects could be overrided concatenating them with a dot "." ("field1.field2")
     */
    String field();

    /**
     * Value to be overridden. If you want to generate it from regex expression use isRegexPattern=true
     */
    String value();

    Overrider.OverriderType type() default Overrider.OverriderType.STRING;

}
