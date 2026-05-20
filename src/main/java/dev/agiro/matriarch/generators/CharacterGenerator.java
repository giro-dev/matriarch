package dev.agiro.matriarch.generators;


import dev.agiro.matriarch.domain.core.GenerationContext;
import dev.agiro.matriarch.domain.model.Definition;

public class CharacterGenerator extends AbstractGenerator<Character> {

    public CharacterGenerator() {
        super(Character.class);
    }

    @Override
    public Character generate(Definition supplierInput) {
        return (char) ('a' + GenerationContext.getInstance().getRandom().nextInt(26));
    }
}
