package dev.agiro.matriarch.suppliers.personal;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random gender values.
 */
public class GenderSupplier extends RandomSupplier<String> {

    private static final String[] GENDERS = {
            "Male", "Female", "Non-binary", "Other", "Prefer not to say"
    };

    @Override
    public String get() {
        return randomElement(GENDERS);
    }
}

