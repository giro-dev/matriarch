package dev.agiro.matriarch.suppliers.util;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random boolean values.
 */
public class BooleanSupplier extends RandomSupplier<Boolean> {

    @Override
    public Boolean get() {
        return RANDOM.nextBoolean();
    }
}

