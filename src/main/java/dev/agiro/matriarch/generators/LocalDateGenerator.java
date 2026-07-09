package dev.agiro.matriarch.generators;


import dev.agiro.matriarch.domain.core.GenerationContext;
import dev.agiro.matriarch.domain.model.Definition;

import java.time.LocalDate;
import java.util.Date;

public class LocalDateGenerator extends AbstractGenerator<LocalDate> {

    public LocalDateGenerator() {
        super(LocalDate.class);
    }

    @Override
    public LocalDate generate(Definition supplierInput) {
        final Date date = new Date(System.currentTimeMillis() - Math.abs(GenerationContext.getInstance().getRandom().nextLong()));
        return new java.sql.Date(date.getTime()).toLocalDate();
    }
}
