package dev.agiro.matriarch.suppliers.util;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random status values.
 */
public class EnumValueSupplier extends RandomSupplier<String> {

    private final String[] values;

    public EnumValueSupplier(Class<? extends Enum<?>> enumType) {
        Enum<?>[] constants = enumType.getEnumConstants();
        if (constants == null) {
            this.values = new String[0];
        } else {
            this.values = new String[constants.length];
            for (int i = 0; i < constants.length; i++) {
                this.values[i] = constants[i].name();
            }
        }
    }

    @Override
    public String get() {
        return randomElement(values);
    }
}

