package dev.agiro.matriarch.generators;

import dev.agiro.matriarch.domain.core.GenerationContext;
import dev.agiro.matriarch.domain.model.Definition;

public class EnumGenerator extends AbstractGenerator<Enum> {

    public EnumGenerator() {
        super(Enum.class);
    }

    @Override
    public Enum<?> generate(Definition supplierInput) {
        Class<? extends Enum> enumClass = (Class<? extends Enum>) supplierInput.clazz();
        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        return enumConstants[GenerationContext.getInstance().getRandom().nextInt(enumConstants.length)];
    }
}
