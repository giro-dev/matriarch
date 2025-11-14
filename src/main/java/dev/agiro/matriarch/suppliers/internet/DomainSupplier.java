package dev.agiro.matriarch.suppliers.internet;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random domain names.
 */
public class DomainSupplier extends RandomSupplier<String> {

    private static final String[] WORDS = {
            "tech", "digital", "web", "cloud", "data", "info", "net", "online",
            "solutions", "systems", "services", "group", "global", "world", "connect",
            "smart", "pro", "expert", "hub", "zone", "site", "portal", "link"
    };

    private static final String[] TLDS = {
            ".com", ".net", ".org", ".io", ".co", ".app", ".dev", ".tech",
            ".online", ".site", ".store", ".info", ".biz", ".me"
    };

    @Override
    public String get() {
        String word = randomElement(WORDS);
        String tld = randomElement(TLDS);

        // Sometimes add a number
        if (RANDOM.nextBoolean()) {
            return word + randomInt(1, 999) + tld;
        }

        return word + tld;
    }
}

