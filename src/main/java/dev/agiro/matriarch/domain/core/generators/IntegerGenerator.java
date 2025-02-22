package dev.agiro.matriarch.domain.core.generators;

import dev.agiro.matriarch.domain.model.FieldProperties;

import java.security.SecureRandom;

public class IntegerGenerator extends AbstractGenerator<Integer> {

    @Override
    Class<Integer> getClazz() {
        return Integer.class;
    }

    @Override
    Integer generate(FieldProperties supplierInput) {
        return new SecureRandom().nextInt();
    }
}
