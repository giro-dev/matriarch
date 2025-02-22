package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.FieldProperties;

import java.security.SecureRandom;

public class LongGenerator extends AbstractGenerator<Long> {

    @Override
    Class<Long> getClazz() {
        return Long.class;
    }

    @Override
    Long generate(FieldProperties supplierInput) {
        return new SecureRandom().nextLong();
    }
}
