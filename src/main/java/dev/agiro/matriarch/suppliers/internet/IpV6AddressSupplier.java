package dev.agiro.matriarch.suppliers.internet;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random IPv6 addresses.
 */
public class IpV6AddressSupplier extends RandomSupplier<String> {

    @Override
    public String get() {
        StringBuilder ipv6 = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            if (i > 0) {
                ipv6.append(":");
            }
            // Generate 4 hex digits
            ipv6.append(String.format("%04x", RANDOM.nextInt(0x10000)));
        }

        return ipv6.toString();
    }
}

