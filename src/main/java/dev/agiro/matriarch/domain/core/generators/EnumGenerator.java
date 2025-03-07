package dev.agiro.matriarch.domain.core.generators;

import dev.agiro.matriarch.domain.model.Definition;

import java.security.SecureRandom;

public class EnumGenerator extends AbstractGenerator<Enum> {

    public EnumGenerator() {
        super(Enum.class);
    }

    @Override
    public Enum<?> generate(Definition supplierInput) {
        Class<? extends Enum> enumClass = (Class<? extends Enum>) supplierInput.clazz();
        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        return enumConstants[new SecureRandom().nextInt(enumConstants.length)];
    }
}
