package dev.agiro.matriarch.suppliers.financial;

import dev.agiro.matriarch.suppliers.base.ConfigurableSupplier;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Supplier that generates random prices.
 * Default range is 1.00 to 999.99.
 */
public class PriceSupplier extends ConfigurableSupplier<BigDecimal> {

    private final double minPrice;
    private final double maxPrice;

    /**
     * Creates a supplier with default range (1.00 to 999.99).
     */
    public PriceSupplier() {
        this(1.0, 999.99);
    }

    /**
     * Creates a supplier with custom price range.
     *
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     */
    public PriceSupplier(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < minPrice) {
            throw new IllegalArgumentException("Invalid price range");
        }
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public BigDecimal get() {
        double range = maxPrice - minPrice;
        double randomPrice = minPrice + (RANDOM.nextDouble() * range);
        return BigDecimal.valueOf(randomPrice).setScale(2, RoundingMode.HALF_UP);
    }
}

