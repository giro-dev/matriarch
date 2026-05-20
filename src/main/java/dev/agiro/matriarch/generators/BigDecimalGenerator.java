package dev.agiro.matriarch.generators;


import dev.agiro.matriarch.domain.core.GenerationContext;
import dev.agiro.matriarch.domain.model.Definition;

import java.math.BigDecimal;

public class BigDecimalGenerator extends AbstractGenerator<BigDecimal> {

    public static final Class<?> type = BigDecimal.class;

    public BigDecimalGenerator() {
        super(BigDecimal.class);
    }

    @Override
    public BigDecimal generate(Definition supplierInput) {
        return BigDecimal.valueOf(GenerationContext.getInstance().getRandom().nextDouble());
    }
}
