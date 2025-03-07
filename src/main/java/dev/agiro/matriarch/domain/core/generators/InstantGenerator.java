package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.Definition;

import java.security.SecureRandom;
import java.time.Instant;

public class InstantGenerator extends AbstractGenerator<Instant> {

    public InstantGenerator() {
        super(Instant.class);
    }

    @Override
    public Instant generate(Definition supplierInput) {
        return Instant.ofEpochMilli(new SecureRandom().nextLong() % Instant.now().toEpochMilli());
    }
}

