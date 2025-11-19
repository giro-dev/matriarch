package dev.agiro.matriarch.suppliers.numeric;

import dev.agiro.matriarch.suppliers.base.ConfigurableSupplier;

/**
 * Supplier that generates random positive integers.
 * Default range is 1-1000.
 */
public class PositiveIntegerSupplier extends ConfigurableSupplier<Integer> {

    private final int min;
    private final int max;

    /**
     * Creates a supplier with default range (1-1000).
     */
    public PositiveIntegerSupplier() {
        this(1, 1000);
    }

    /**
     * Creates a supplier with custom range.
     *
     * @param min minimum value (must be positive)
     * @param max maximum value (exclusive)
     */
    public PositiveIntegerSupplier(int min, int max) {
        if (min < 1) {
            throw new IllegalArgumentException("min must be at least 1");
        }
        if (max <= min) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        this.min = min;
        this.max = max;
    }

    @Override
    public Integer get() {
        return randomInt(min, max);
    }
}

