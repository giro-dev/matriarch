package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.FieldProperties;

import java.math.BigDecimal;
import java.security.SecureRandom;

public class BigDecimalGenerator extends AbstractGenerator<BigDecimal> {

    @Override
    Class<BigDecimal> getClazz() {
        return BigDecimal.class;
    }

    @Override
    BigDecimal generate(FieldProperties supplierInput) {
        return BigDecimal.valueOf(new SecureRandom().nextDouble());
    }
}
