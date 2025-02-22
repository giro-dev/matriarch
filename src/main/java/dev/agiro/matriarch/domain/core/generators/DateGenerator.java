package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.FieldProperties;

import java.security.SecureRandom;
import java.util.Date;

public class DateGenerator extends AbstractGenerator<Date> {

    @Override
    Class<Date> getClazz() {
        return Date.class;
    }

    @Override
    Date generate(FieldProperties supplierInput) {
        return new Date(System.currentTimeMillis() - new SecureRandom().nextLong());
    }
}
