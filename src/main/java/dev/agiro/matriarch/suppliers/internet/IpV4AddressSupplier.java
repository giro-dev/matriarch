package dev.agiro.matriarch.suppliers.internet;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random IPv4 addresses.
 */
public class IpV4AddressSupplier extends RandomSupplier<String> {

    @Override
    public String get() {
        int octet1 = RANDOM.nextInt(256);
        int octet2 = RANDOM.nextInt(256);
        int octet3 = RANDOM.nextInt(256);
        int octet4 = RANDOM.nextInt(256);

        return String.format("%d.%d.%d.%d", octet1, octet2, octet3, octet4);
    }
}

