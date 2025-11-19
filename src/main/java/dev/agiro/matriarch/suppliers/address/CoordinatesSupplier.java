package dev.agiro.matriarch.suppliers.address;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random coordinates as a formatted string.
 * Format: "latitude, longitude" (e.g., "40.7128, -74.0060")
 */
public class CoordinatesSupplier extends RandomSupplier<String> {

    private final LatitudeSupplier latitudeSupplier = new LatitudeSupplier();
    private final LongitudeSupplier longitudeSupplier = new LongitudeSupplier();

    @Override
    public String get() {
        double latitude = latitudeSupplier.get();
        double longitude = longitudeSupplier.get();
        return String.format(java.util.Locale.US, "%.6f, %.6f", latitude, longitude);
    }
}
