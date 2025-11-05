package dev.agiro.matriarch.generators;


import dev.agiro.matriarch.domain.model.Definition;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;

public class TimestampGenerator extends AbstractGenerator<Timestamp> {

    public TimestampGenerator() {
        super(Timestamp.class);
    }

    @Override
    public Timestamp generate(Definition supplierInput) {
        return Timestamp.from(Instant.ofEpochMilli(Math.absExact(new SecureRandom().nextLong()) % Instant.now().toEpochMilli()));
    }
}

