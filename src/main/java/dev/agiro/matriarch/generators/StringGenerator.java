package dev.agiro.matriarch.generators;


import dev.agiro.matriarch.domain.core.GenerationContext;
import dev.agiro.matriarch.domain.model.Definition;

public class StringGenerator extends AbstractGenerator<String> {

    public StringGenerator() {
        super(String.class);
    }

    @Override
    public String generate(Definition def) {
        return def.overrideCoordinate() + "_" + GenerationContext.getInstance().getRandom().nextInt(1000, 9999);
    }
}
