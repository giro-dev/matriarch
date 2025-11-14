package dev.agiro.matriarch.suppliers.personal;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random usernames.
 */
public class UsernameSupplier extends RandomSupplier<String> {

    private static final String[] ADJECTIVES = {
            "cool", "super", "mega", "ultra", "awesome", "epic", "legendary", "mighty",
            "swift", "silent", "hidden", "golden", "silver", "dark", "bright", "wild"
    };

    private static final String[] NOUNS = {
            "tiger", "dragon", "phoenix", "wolf", "eagle", "falcon", "lion", "bear",
            "ninja", "warrior", "knight", "wizard", "hunter", "ranger", "scout", "pilot"
    };

    @Override
    public String get() {
        int pattern = RANDOM.nextInt(3);
        return switch (pattern) {
            case 0 -> randomElement(ADJECTIVES) + randomElement(NOUNS) + randomInt(1, 999);
            case 1 -> randomElement(NOUNS) + randomInt(100, 9999);
            default -> randomElement(ADJECTIVES) + "_" + randomElement(NOUNS);
        };
    }
}

