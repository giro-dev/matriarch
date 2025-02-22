package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.FieldProperties;

import java.security.SecureRandom;
import java.time.Instant;

public class InstantGenerator extends AbstractGenerator<Instant> {

    @Override
    Class<Instant> getClazz() {
        return Instant.class;
    }

    @Override
    Instant generate(FieldProperties supplierInput) {
        return Instant.ofEpochMilli(new SecureRandom().nextLong() % Instant.now().toEpochMilli());
    }
}

