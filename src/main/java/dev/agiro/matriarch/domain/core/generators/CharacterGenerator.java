package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.FieldProperties;

import java.security.SecureRandom;

public class CharacterGenerator extends AbstractGenerator<Character> {

    @Override
    Class<Character> getClazz() {
        return Character.class;
    }

    @Override
    Character generate(FieldProperties supplierInput) {
        return (char) ('a' + new SecureRandom().nextInt(26));
    }
}
