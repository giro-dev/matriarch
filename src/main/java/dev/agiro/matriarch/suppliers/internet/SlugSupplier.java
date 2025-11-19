package dev.agiro.matriarch.suppliers.internet;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random URL-friendly slugs.
 */
public class SlugSupplier extends RandomSupplier<String> {

    private static final String[] WORDS = {
            "awesome", "great", "super", "amazing", "fantastic", "wonderful", "excellent",
            "article", "post", "guide", "tutorial", "tips", "tricks", "how-to",
            "best", "top", "ultimate", "complete", "comprehensive", "quick", "easy",
            "beginner", "advanced", "professional", "expert", "introduction", "overview",
            "web", "mobile", "app", "software", "tech", "digital", "online", "cloud",
            "java", "python", "javascript", "programming", "development", "design"
    };

    @Override
    public String get() {
        int wordCount = randomInt(2, 5);
        StringBuilder slug = new StringBuilder();

        for (int i = 0; i < wordCount; i++) {
            if (i > 0) {
                slug.append("-");
            }
            slug.append(randomElement(WORDS));
        }

        return slug.toString();
    }
}

