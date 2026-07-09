package dev.agiro.matriarch.generators;


import dev.agiro.matriarch.domain.core.GenerationContext;
import dev.agiro.matriarch.domain.model.Definition;

import java.time.Instant;

public class InstantGenerator extends AbstractGenerator<Instant> {

    public InstantGenerator() {
        super(Instant.class);
    }

    @Override
    public Instant generate(Definition supplierInput) {
        return Instant.ofEpochMilli(GenerationContext.getInstance().getRandom().nextLong() % Instant.now().toEpochMilli());
    }
}

