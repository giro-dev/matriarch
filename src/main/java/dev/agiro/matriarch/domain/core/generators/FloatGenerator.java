package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.FieldProperties;

import java.security.SecureRandom;

public class FloatGenerator extends AbstractGenerator<Float> {

    @Override
    Class<Float> getClazz() {
        return Float.class;
    }

    @Override
    Float generate(FieldProperties supplierInput) {
        return new SecureRandom().nextFloat();
    }
}
