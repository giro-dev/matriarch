package dev.agiro.matriarch.suppliers.internet;

import dev.agiro.matriarch.suppliers.base.ConfigurableSupplier;

/**
 * Supplier that generates random passwords.
 * Default length is 12 characters with mixed case, digits, and special characters.
 */
public class PasswordSupplier extends ConfigurableSupplier<String> {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()_+-=[]{}|;:,.<>?";

    private final int length;
    private final boolean includeSpecialChars;

    /**
     * Creates a password supplier with default settings (12 chars, with special chars).
     */
    public PasswordSupplier() {
        this(12, true);
    }

    /**
     * Creates a password supplier with custom length.
     *
     * @param length password length
     */
    public PasswordSupplier(int length) {
        this(length, true);
    }

    /**
     * Creates a password supplier with custom settings.
     *
     * @param length              password length
     * @param includeSpecialChars whether to include special characters
     */
    public PasswordSupplier(int length, boolean includeSpecialChars) {
        if (length < 4) {
            throw new IllegalArgumentException("Password length must be at least 4");
        }
        this.length = length;
        this.includeSpecialChars = includeSpecialChars;
    }

    @Override
    public String get() {
        String chars = LOWERCASE + UPPERCASE + DIGITS;
        if (includeSpecialChars) {
            chars += SPECIAL;
        }

        StringBuilder password = new StringBuilder(length);

        // Ensure at least one of each required type
        password.append(LOWERCASE.charAt(RANDOM.nextInt(LOWERCASE.length())));
        password.append(UPPERCASE.charAt(RANDOM.nextInt(UPPERCASE.length())));
        password.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));

        if (includeSpecialChars && length > 3) {
            password.append(SPECIAL.charAt(RANDOM.nextInt(SPECIAL.length())));
        }

        // Fill remaining with random characters
        int remaining = length - password.length();
        for (int i = 0; i < remaining; i++) {
            password.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }

        // Shuffle the password
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }

        return new String(passwordArray);
    }
}

