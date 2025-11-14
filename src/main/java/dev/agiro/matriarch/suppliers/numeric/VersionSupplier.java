package dev.agiro.matriarch.suppliers.numeric;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random semantic version numbers.
 * Format: MAJOR.MINOR.PATCH (e.g., "1.2.3")
 */
public class VersionSupplier extends RandomSupplier<String> {

    @Override
    public String get() {
        int major = RANDOM.nextInt(10);      // 0-9
        int minor = RANDOM.nextInt(20);      // 0-19
        int patch = RANDOM.nextInt(100);     // 0-99

        return String.format("%d.%d.%d", major, minor, patch);
    }
}

