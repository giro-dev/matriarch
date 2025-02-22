package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.FieldProperties;

import java.security.SecureRandom;

public class BooleanGenerator extends AbstractGenerator<Boolean> {

    @Override
    Class<Boolean> getClazz() {
        return Boolean.class;
    }

    @Override
    Boolean generate(FieldProperties supplierInput) {
        return new SecureRandom().nextBoolean();
    }
}
