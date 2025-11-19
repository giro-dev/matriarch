package dev.agiro.matriarch.suppliers.numeric;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random percentage values.
 * Returns integer values between 0 and 100 (inclusive).
 */
public class PercentageSupplier extends RandomSupplier<Integer> {

    @Override
    public Integer get() {
        return RANDOM.nextInt(101); // 0 to 100 inclusive
    }
}

