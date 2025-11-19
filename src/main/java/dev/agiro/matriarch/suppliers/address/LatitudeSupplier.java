package dev.agiro.matriarch.suppliers.address;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random latitude coordinates.
 * Range: -90.0 to 90.0 degrees.
 */
public class LatitudeSupplier extends RandomSupplier<Double> {

    @Override
    public Double get() {
        // Generate latitude between -90 and 90
        return -90.0 + (RANDOM.nextDouble() * 180.0);
    }
}

