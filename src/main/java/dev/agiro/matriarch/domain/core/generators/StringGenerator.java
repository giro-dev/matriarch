package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.FieldProperties;

public class StringGenerator extends AbstractGenerator<String> {

    @Override
    Class<String> getClazz() {
        return String.class;
    }

    @Override
    String generate(FieldProperties supplierInput) {
        return "";
    }
}
