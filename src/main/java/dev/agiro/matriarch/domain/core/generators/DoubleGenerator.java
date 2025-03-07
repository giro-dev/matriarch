package dev.agiro.matriarch.domain.core.generators;



import dev.agiro.matriarch.domain.model.Definition;

import java.security.SecureRandom;

public class DoubleGenerator extends AbstractGenerator<Double> {

    public DoubleGenerator() {
        super(Double.class);
    }

    @Override
    public Double generate(Definition supplierInput) {
        return new SecureRandom().nextDouble();
    }
}
