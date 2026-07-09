package dev.agiro.matriarch.generators;


import dev.agiro.matriarch.domain.core.GenerationContext;
import dev.agiro.matriarch.domain.model.Definition;

public class BooleanGenerator extends AbstractGenerator<Boolean> {

    public BooleanGenerator() {
        super(Boolean.class);
    }

    @Override
    public Boolean generate(Definition supplierInput) {
        return GenerationContext.getInstance().getRandom().nextBoolean();
    }
}
