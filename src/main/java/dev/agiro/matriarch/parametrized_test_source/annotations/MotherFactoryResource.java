package dev.agiro.matriarch.parametrized_test_source.annotations;


import dev.agiro.matriarch.parametrized_test_source.MotherFactoryResourceProviders;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(MotherFactoryResourceProviders.class)
public @interface MotherFactoryResource {
    RandomArg[] args() ;
}
