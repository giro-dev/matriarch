package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.FieldProperties;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

public class LocalDateTimeGenerator extends AbstractGenerator<LocalDateTime> {

    @Override
    Class<LocalDateTime> getClazz() {
        return LocalDateTime.class;
    }

    @Override
    LocalDateTime generate(FieldProperties supplierInput) {
        final Date date = new Date(System.currentTimeMillis() - new SecureRandom().nextLong());
        final Random random = new SecureRandom();
        final int hour = random.nextInt(1, 23);
        final int minute = random.nextInt(1, 59);
        return new java.sql.Date(date.getTime()).toLocalDate().atTime(hour, minute);
    }
}
