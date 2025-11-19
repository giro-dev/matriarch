package dev.agiro.matriarch.suppliers.text;

import dev.agiro.matriarch.suppliers.base.ConfigurableSupplier;

/**
 * Supplier that generates random paragraphs.
 * Default paragraph contains 3-7 sentences.
 */
public class ParagraphSupplier extends ConfigurableSupplier<String> {

    private final SentenceSupplier sentenceSupplier = new SentenceSupplier();
    private final int minSentences;
    private final int maxSentences;

    /**
     * Creates a supplier with default sentence count (3-7 sentences).
     */
    public ParagraphSupplier() {
        this(3, 7);
    }

    /**
     * Creates a supplier with custom sentence count range.
     *
     * @param minSentences minimum sentences in paragraph
     * @param maxSentences maximum sentences in paragraph
     */
    public ParagraphSupplier(int minSentences, int maxSentences) {
        if (minSentences < 1 || maxSentences < minSentences) {
            throw new IllegalArgumentException("Invalid sentence count range");
        }
        this.minSentences = minSentences;
        this.maxSentences = maxSentences;
    }

    @Override
    public String get() {
        int sentenceCount = randomInt(minSentences, maxSentences + 1);
        StringBuilder paragraph = new StringBuilder();

        for (int i = 0; i < sentenceCount; i++) {
            if (i > 0) {
                paragraph.append(" ");
            }
            paragraph.append(sentenceSupplier.get());
        }

        return paragraph.toString();
    }
}

