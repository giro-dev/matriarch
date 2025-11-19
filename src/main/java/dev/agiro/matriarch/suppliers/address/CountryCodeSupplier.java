package dev.agiro.matriarch.suppliers.address;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random ISO 3166-1 alpha-2 country codes.
 */
public class CountryCodeSupplier extends RandomSupplier<String> {

    private static final String[] COUNTRY_CODES = {
            "US", "CA", "GB", "DE", "FR", "IT", "ES", "NL", "BE", "CH",
            "AT", "SE", "NO", "DK", "FI", "PL", "CZ", "PT", "GR", "IE",
            "AU", "NZ", "JP", "KR", "CN", "IN", "BR", "MX", "AR", "CL",
            "CO", "PE", "ZA", "EG", "TR", "IL", "AE", "SA", "SG", "MY",
            "TH", "ID", "PH", "VN", "RU", "UA", "RO", "HU"
    };

    @Override
    public String get() {
        return randomElement(COUNTRY_CODES);
    }
}

