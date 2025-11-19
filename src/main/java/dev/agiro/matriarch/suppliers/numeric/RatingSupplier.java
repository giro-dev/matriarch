package dev.agiro.matriarch.suppliers.numeric;

import dev.agiro.matriarch.suppliers.base.ConfigurableSupplier;

/**
 * Supplier that generates random rating values.
 * Default range is 1-5 stars.
 */
public class RatingSupplier extends ConfigurableSupplier<Integer> {

    private final int minRating;
    private final int maxRating;

    /**
     * Creates a supplier with default 5-star rating (1-5).
     */
    public RatingSupplier() {
        this(1, 5);
    }

    /**
     * Creates a supplier with custom rating range.
     *
     * @param minRating minimum rating (inclusive)
     * @param maxRating maximum rating (inclusive)
     */
    public RatingSupplier(int minRating, int maxRating) {
        if (minRating < 0 || maxRating < minRating) {
            throw new IllegalArgumentException("Invalid rating range");
        }
        this.minRating = minRating;
        this.maxRating = maxRating;
    }

    @Override
    public Integer get() {
        return randomInt(minRating, maxRating + 1);
    }
}

