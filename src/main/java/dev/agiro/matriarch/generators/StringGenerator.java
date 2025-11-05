package dev.agiro.matriarch.generators;


import dev.agiro.matriarch.domain.model.Definition;

import java.security.SecureRandom;

public class StringGenerator extends AbstractGenerator<String> {

    public StringGenerator() {
        super(String.class);
    }

    @Override
    public String generate(Definition def) {
        return def.overrideCoordinate() + "_" + new SecureRandom().nextInt(1000, 9999);
    }
}
