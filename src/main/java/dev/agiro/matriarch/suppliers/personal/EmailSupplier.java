package dev.agiro.matriarch.suppliers.personal;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random email addresses.
 */
public class EmailSupplier extends RandomSupplier<String> {

    private static final String[] DOMAINS = {
            "gmail.com", "yahoo.com", "outlook.com", "hotmail.com", "icloud.com",
            "protonmail.com", "mail.com", "aol.com", "zoho.com", "fastmail.com"
    };

    private final FirstNameSupplier firstNameSupplier = new FirstNameSupplier();
    private final LastNameSupplier lastNameSupplier = new LastNameSupplier();

    @Override
    public String get() {
        String firstName = firstNameSupplier.get().toLowerCase();
        String lastName = lastNameSupplier.get().toLowerCase();
        String domain = randomElement(DOMAINS);

        // Various email patterns
        int pattern = RANDOM.nextInt(4);
        return switch (pattern) {
            case 0 -> firstName + "." + lastName + "@" + domain;
            case 1 -> firstName + lastName + "@" + domain;
            case 2 -> firstName + "_" + lastName + "@" + domain;
            default -> firstName + randomInt(1, 999) + "@" + domain;
        };
    }
}

