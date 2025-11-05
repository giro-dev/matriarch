package dev.agiro.matriarch.generators;


import dev.agiro.matriarch.domain.model.Definition;

import java.util.UUID;

public class UUIDGenerator extends AbstractGenerator<UUID> {

    public UUIDGenerator() {
        super(UUID.class);
    }

    @Override
    public UUID generate(Definition supplierInput) {
        return UUID.randomUUID();
    }
}
