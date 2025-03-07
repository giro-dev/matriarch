package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.Definition;

import java.math.BigDecimal;
import java.security.SecureRandom;

public class BigDecimalGenerator extends AbstractGenerator<BigDecimal> {

    public static final Class<?> type = BigDecimal.class;

    public BigDecimalGenerator() {
        super(BigDecimal.class);
    }

    @Override
    public BigDecimal generate(Definition supplierInput) {
        return BigDecimal.valueOf(new SecureRandom().nextDouble());
    }
}
