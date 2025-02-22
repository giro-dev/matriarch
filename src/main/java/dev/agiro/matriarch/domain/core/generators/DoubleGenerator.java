package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.FieldProperties;

import java.security.SecureRandom;

public class DoubleGenerator extends AbstractGenerator<Double> {

    @Override
    Class<Double> getClazz() {
        return Double.class;
    }

    @Override
    Double generate(FieldProperties supplierInput) {
        return new SecureRandom().nextDouble();
    }
}
