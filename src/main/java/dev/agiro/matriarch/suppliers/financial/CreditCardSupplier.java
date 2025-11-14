package dev.agiro.matriarch.suppliers.financial;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random credit card numbers.
 * Note: These are randomly generated numbers for testing only,
 * they do not pass Luhn algorithm validation.
 */
public class CreditCardSupplier extends RandomSupplier<String> {

    private static final String[] PREFIXES = {
            "4",      // Visa
            "51", "52", "53", "54", "55",  // Mastercard
            "34", "37",  // American Express
            "6011"    // Discover
    };

    @Override
    public String get() {
        String prefix = randomElement(PREFIXES);
        boolean isAmex = prefix.startsWith("34") || prefix.startsWith("37");
        int totalLength = isAmex ? 15 : 16;
        int remainingDigits = totalLength - prefix.length();

        StringBuilder raw = new StringBuilder(prefix);
        for (int i = 0; i < remainingDigits; i++) {
            raw.append(RANDOM.nextInt(10));
        }

        if (isAmex) {
            // Amex format: #### ###### ##### (4-6-5)
            String part1 = pad(raw.substring(0, 4));
            String part2 = pad(raw.substring(4, 10));
            String part3 = pad(raw.substring(10));
            return part1 + " " + part2 + " " + part3;
        } else {
            // Standard cards: #### #### #### #### (4-4-4-4)
            StringBuilder formatted = new StringBuilder();
            for (int i = 0; i < raw.length(); i++) {
                if (i > 0 && i % 4 == 0) formatted.append(" ");
                formatted.append(raw.charAt(i));
            }
            return formatted.toString();
        }
    }

    private String pad(String s) {
        return s;
    }
}
