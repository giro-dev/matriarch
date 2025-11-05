package dev.agiro.matriarch.generators;


import dev.agiro.matriarch.domain.model.Definition;

import java.security.SecureRandom;

public class BooleanGenerator extends AbstractGenerator<Boolean> {

    public BooleanGenerator() {
        super(Boolean.class);
    }

    @Override
    public Boolean generate(Definition supplierInput) {
        return new SecureRandom().nextBoolean();
    }
}
