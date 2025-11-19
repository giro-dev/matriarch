package dev.agiro.matriarch.suppliers.personal;

import dev.agiro.matriarch.suppliers.base.ConfigurableSupplier;

/**
 * Supplier that generates random ages.
 * Default range is 18-80 years.
 */
public class AgeSupplier extends ConfigurableSupplier<Integer> {

    private final int minAge;
    private final int maxAge;

    /**
     * Creates an AgeSupplier with default range (18-80).
     */
    public AgeSupplier() {
        this(18, 80);
    }

    /**
     * Creates an AgeSupplier with custom range.
     *
     * @param minAge minimum age (inclusive)
     * @param maxAge maximum age (inclusive)
     */
    public AgeSupplier(int minAge, int maxAge) {
        if (minAge < 0 || maxAge < minAge) {
            throw new IllegalArgumentException("Invalid age range");
        }
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    @Override
    public Integer get() {
        return randomInt(minAge, maxAge + 1);
    }
}

