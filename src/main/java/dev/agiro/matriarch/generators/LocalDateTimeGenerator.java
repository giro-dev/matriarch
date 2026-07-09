package dev.agiro.matriarch.generators;


import dev.agiro.matriarch.domain.core.GenerationContext;
import dev.agiro.matriarch.domain.model.Definition;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

public class LocalDateTimeGenerator extends AbstractGenerator<LocalDateTime> {

    public LocalDateTimeGenerator() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime generate(Definition supplierInput) {
        final Random random = GenerationContext.getInstance().getRandom();
        final Date date = new Date(System.currentTimeMillis() - Math.abs(random.nextLong()));
        final int hour = random.nextInt(1, 23);
        final int minute = random.nextInt(1, 59);
        return new java.sql.Date(date.getTime()).toLocalDate().atTime(hour, minute);
    }
}
