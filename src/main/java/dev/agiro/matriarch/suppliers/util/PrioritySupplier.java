package dev.agiro.matriarch.suppliers.util;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random priority values.
 */
public class PrioritySupplier extends RandomSupplier<String> {

    private static final String[] PRIORITIES = {
            "LOW",
            "MEDIUM",
            "HIGH",
            "CRITICAL",
            "URGENT"
    };

    @Override
    public String get() {
        return randomElement(PRIORITIES);
    }
}

