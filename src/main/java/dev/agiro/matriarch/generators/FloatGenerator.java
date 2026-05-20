package dev.agiro.matriarch.generators;


import dev.agiro.matriarch.domain.core.GenerationContext;
import dev.agiro.matriarch.domain.model.Definition;

public class FloatGenerator extends AbstractGenerator<Float> {

    public FloatGenerator() {
        super(Float.class);
    }

    @Override
    public Float generate(Definition supplierInput) {
        return GenerationContext.getInstance().getRandom().nextFloat();
    }
}
