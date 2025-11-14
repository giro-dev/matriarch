package dev.agiro.matriarch.suppliers.internet;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random MAC addresses.
 * Format: XX:XX:XX:XX:XX:XX
 */
public class MacAddressSupplier extends RandomSupplier<String> {

    @Override
    public String get() {
        StringBuilder mac = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            if (i > 0) {
                mac.append(":");
            }
            mac.append(String.format("%02X", RANDOM.nextInt(256)));
        }

        return mac.toString();
    }
}

