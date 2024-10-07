package dev.agiro.matriarch.core.annotated_test.annotations;

import java.lang.annotation.*;

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

    boolean isRegexPattern() default false;

}
