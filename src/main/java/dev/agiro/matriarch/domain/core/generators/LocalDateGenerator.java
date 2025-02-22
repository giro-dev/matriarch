package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.FieldProperties;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Date;

public class LocalDateGenerator extends AbstractGenerator<LocalDate> {

    @Override
    Class<LocalDate> getClazz() {
        return LocalDate.class;
    }

    @Override
    LocalDate generate(FieldProperties supplierInput) {
        final Date date = new Date(System.currentTimeMillis() -new SecureRandom().nextLong());
        return new java.sql.Date(date.getTime()).toLocalDate();
    }
}
