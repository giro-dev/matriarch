package dev.agiro.matriarch.generators;


import dev.agiro.matriarch.domain.model.Definition;

import java.security.SecureRandom;

public class LongGenerator extends AbstractGenerator<Long> {

    public LongGenerator() {
        super(Long.class);
    }

    @Override
    public Long generate(Definition supplierInput) {
        return new SecureRandom().nextLong();
    }
}
