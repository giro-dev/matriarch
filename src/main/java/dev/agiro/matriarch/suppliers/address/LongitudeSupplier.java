package dev.agiro.matriarch.suppliers.address;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random longitude coordinates.
 * Range: -180.0 to 180.0 degrees.
 */
public class LongitudeSupplier extends RandomSupplier<Double> {

    @Override
    public Double get() {
        // Generate longitude between -180 and 180
        return -180.0 + (RANDOM.nextDouble() * 360.0);
    }
}

