package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.Definition;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Date;

public class LocalDateGenerator extends AbstractGenerator<LocalDate> {

    public LocalDateGenerator() {
        super(LocalDate.class);
    }

    @Override
    public LocalDate generate(Definition supplierInput) {
        final Date date = new Date(System.currentTimeMillis() -new SecureRandom().nextLong());
        return new java.sql.Date(date.getTime()).toLocalDate();
    }
}
