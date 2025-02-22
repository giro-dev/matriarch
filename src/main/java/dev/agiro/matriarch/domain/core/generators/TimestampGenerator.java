package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.FieldProperties;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;

public class TimestampGenerator extends AbstractGenerator<Timestamp> {

    @Override
    Class<Timestamp> getClazz() {
        return Timestamp.class;
    }

    @Override
    Timestamp generate(FieldProperties supplierInput) {
        return Timestamp.from(Instant.ofEpochMilli(Math.abs(new SecureRandom().nextLong()) % Instant.now().toEpochMilli()));
    }
}

