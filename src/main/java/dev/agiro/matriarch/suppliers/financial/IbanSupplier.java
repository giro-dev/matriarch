package dev.agiro.matriarch.suppliers.financial;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random IBAN (International Bank Account Number) strings.
 * Note: These are randomly formatted IBANs for testing only,
 * they do not pass checksum validation.
 */
public class IbanSupplier extends RandomSupplier<String> {

    private static final String[] COUNTRY_CODES = {
            "DE", "FR", "GB", "IT", "ES", "NL", "BE", "AT", "CH", "SE", "NO", "DK", "FI", "PL"
    };

    @Override
    public String get() {
        String countryCode = randomElement(COUNTRY_CODES);
        int checkDigits = randomInt(10, 99);

        // Decide whether to generate 4 or 5 groups of 4 digits (regex allows {4,5})
        int groups = RANDOM.nextBoolean() ? 4 : 5;
        StringBuilder sb = new StringBuilder();
        sb.append(countryCode).append(checkDigits);
        for (int i = 0; i < groups; i++) {
            sb.append(" ");
            for (int d = 0; d < 4; d++) {
                sb.append(RANDOM.nextInt(10));
            }
        }
        return sb.toString();
    }
}
