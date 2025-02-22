package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.FieldProperties;

import java.util.UUID;

public class UUIDGenerator extends AbstractGenerator<UUID> {

    @Override
    Class<UUID> getClazz() {
        return UUID.class;
    }

    @Override
    UUID generate(FieldProperties supplierInput) {
        return UUID.randomUUID();
    }
}
