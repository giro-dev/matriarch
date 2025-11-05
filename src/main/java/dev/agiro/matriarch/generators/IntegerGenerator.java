package dev.agiro.matriarch.generators;

import dev.agiro.matriarch.domain.model.Definition;

import java.security.SecureRandom;

public class IntegerGenerator extends AbstractGenerator<Integer> {


    public IntegerGenerator() {
        super(Integer.class);
    }

    @Override
    public Class<Integer> getClazz() {
        return Integer.class;
    }

    @Override
    public Integer generate(Definition supplierInput) {
        return Math.absExact(new SecureRandom().nextInt());
    }
}
