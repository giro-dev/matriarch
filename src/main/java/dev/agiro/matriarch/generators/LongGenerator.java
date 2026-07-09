package dev.agiro.matriarch.generators;


import dev.agiro.matriarch.domain.core.GenerationContext;
import dev.agiro.matriarch.domain.model.Definition;

public class LongGenerator extends AbstractGenerator<Long> {

    public LongGenerator() {
        super(Long.class);
    }

    @Override
    public Long generate(Definition supplierInput) {
        return GenerationContext.getInstance().getRandom().nextLong();
    }
}
