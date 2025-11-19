package dev.agiro.matriarch.suppliers.text;

import dev.agiro.matriarch.suppliers.base.ConfigurableSupplier;

/**
 * Supplier that generates Lorem Ipsum text.
 * Default generates a single paragraph.
 */
public class LoremIpsumSupplier extends ConfigurableSupplier<String> {

    private static final String[] WORDS = {
            "lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit",
            "sed", "do", "eiusmod", "tempor", "incididunt", "ut", "labore", "et", "dolore",
            "magna", "aliqua", "enim", "ad", "minim", "veniam", "quis", "nostrud",
            "exercitation", "ullamco", "laboris", "nisi", "aliquip", "ex", "ea", "commodo",
            "consequat", "duis", "aute", "irure", "in", "reprehenderit", "voluptate",
            "velit", "esse", "cillum", "fugiat", "nulla", "pariatur", "excepteur", "sint",
            "occaecat", "cupidatat", "non", "proident", "sunt", "culpa", "qui", "officia",
            "deserunt", "mollit", "anim", "id", "est", "laborum"
    };

    private final int wordCount;

    /**
     * Creates a supplier with default word count (50 words).
     */
    public LoremIpsumSupplier() {
        this(50);
    }

    /**
     * Creates a supplier with custom word count.
     *
     * @param wordCount number of words to generate
     */
    public LoremIpsumSupplier(int wordCount) {
        if (wordCount < 1) {
            throw new IllegalArgumentException("wordCount must be positive");
        }
        this.wordCount = wordCount;
    }

    @Override
    public String get() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < wordCount; i++) {
            if (i > 0) {
                result.append(" ");
            }

            String word = randomElement(WORDS);

            // Capitalize first word
            if (i == 0) {
                word = Character.toUpperCase(word.charAt(0)) + word.substring(1);
            }

            result.append(word);

            // Add punctuation
            if ((i + 1) % randomInt(8, 15) == 0 && i < wordCount - 1) {
                result.append(",");
            }
        }

        result.append(".");
        return result.toString();
    }
}

