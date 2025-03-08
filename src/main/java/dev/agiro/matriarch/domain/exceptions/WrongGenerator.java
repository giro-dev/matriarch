package dev.agiro.matriarch.domain.exceptions;

import dev.agiro.matriarch.domain.core.generators.AbstractGenerator;

public class WrongGenerator extends RuntimeException {
    public <L> WrongGenerator(AbstractGenerator<?> generator, Class<?> aClass) {
        super("Generator '%s' is not able to generate type %s".formatted(generator.getClass().getSimpleName(), aClass.getName()));
    }
}
