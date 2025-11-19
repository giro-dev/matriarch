package dev.agiro.matriarch.suppliers.text;

import dev.agiro.matriarch.suppliers.base.ConfigurableSupplier;

/**
 * Supplier that generates random sentences.
 * Default sentence contains 5-15 words.
 */
public class SentenceSupplier extends ConfigurableSupplier<String> {

    private final WordSupplier wordSupplier = new WordSupplier();
    private final int minWords;
    private final int maxWords;

    /**
     * Creates a supplier with default word count (5-15 words).
     */
    public SentenceSupplier() {
        this(5, 15);
    }

    /**
     * Creates a supplier with custom word count range.
     *
     * @param minWords minimum words in sentence
     * @param maxWords maximum words in sentence
     */
    public SentenceSupplier(int minWords, int maxWords) {
        if (minWords < 1 || maxWords < minWords) {
            throw new IllegalArgumentException("Invalid word count range");
        }
        this.minWords = minWords;
        this.maxWords = maxWords;
    }

    @Override
    public String get() {
        int wordCount = randomInt(minWords, maxWords + 1);
        StringBuilder sentence = new StringBuilder();

        for (int i = 0; i < wordCount; i++) {
            if (i > 0) {
                sentence.append(" ");
            }

            String word = wordSupplier.get();

            // Capitalize first word
            if (i == 0) {
                word = Character.toUpperCase(word.charAt(0)) + word.substring(1);
            }

            sentence.append(word);
        }

        sentence.append(".");
        return sentence.toString();
    }
}

