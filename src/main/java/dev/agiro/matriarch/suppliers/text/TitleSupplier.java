package dev.agiro.matriarch.suppliers.text;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random titles (book/article titles).
 */
public class TitleSupplier extends RandomSupplier<String> {

    private static final String[] ADJECTIVES = {
            "Ultimate", "Complete", "Essential", "Comprehensive", "Practical", "Advanced",
            "Modern", "Effective", "Powerful", "Simple", "Quick", "Professional",
            "Expert", "Beginner's", "Definitive", "Absolute", "Revolutionary", "Innovative"
    };

    private static final String[] NOUNS = {
            "Guide", "Handbook", "Manual", "Tutorial", "Introduction", "Reference",
            "Journey", "Adventure", "Story", "Tale", "Chronicle", "Secrets", "Methods",
            "Techniques", "Strategies", "Principles", "Foundations", "Mastery"
    };

    private static final String[] TOPICS = {
            "Programming", "Design", "Leadership", "Success", "Innovation", "Technology",
            "Business", "Marketing", "Finance", "Management", "Development", "Strategy",
            "Architecture", "Engineering", "Science", "Art", "Writing", "Communication"
    };

    private static final String[] PREPOSITIONS = {
            "to", "for", "of", "in", "on", "with", "about"
    };

    @Override
    public String get() {
        int pattern = RANDOM.nextInt(4);

        return switch (pattern) {
            case 0 -> String.format("The %s %s %s %s",
                    randomElement(ADJECTIVES),
                    randomElement(NOUNS),
                    randomElement(PREPOSITIONS),
                    randomElement(TOPICS));
            case 1 -> String.format("%s %s: A %s Approach",
                    randomElement(TOPICS),
                    randomElement(NOUNS),
                    randomElement(ADJECTIVES));
            case 2 -> String.format("%s %s",
                    randomElement(ADJECTIVES),
                    randomElement(TOPICS));
            default -> String.format("Mastering %s: The %s Guide",
                    randomElement(TOPICS),
                    randomElement(ADJECTIVES));
        };
    }
}

