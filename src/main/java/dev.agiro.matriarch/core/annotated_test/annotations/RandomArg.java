package dev.agiro.matriarch.core.annotated_test.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RandomArg {
    Class<? extends Object> targetClass();
    OverrideField[] overrides() default {};
    String jsonOverrides() default "";
}
