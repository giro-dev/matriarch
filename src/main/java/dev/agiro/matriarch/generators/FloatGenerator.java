package dev.agiro.matriarch.generators;


import dev.agiro.matriarch.domain.model.Definition;

import java.security.SecureRandom;

public class FloatGenerator extends AbstractGenerator<Float> {

    public FloatGenerator() {
        super(Float.class);
    }

    @Override
    public Float generate(Definition supplierInput) {
        return new SecureRandom().nextFloat();
    }
}
