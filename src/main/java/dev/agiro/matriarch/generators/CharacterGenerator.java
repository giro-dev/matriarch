package dev.agiro.matriarch.generators;


import dev.agiro.matriarch.domain.model.Definition;

import java.security.SecureRandom;

public class CharacterGenerator extends AbstractGenerator<Character> {

    public CharacterGenerator() {
        super(Character.class);
    }

    @Override
    public Character generate(Definition supplierInput) {
        return (char) ('a' + new SecureRandom().nextInt(26));
    }
}
