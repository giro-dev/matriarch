package dev.agiro.matriarch.suppliers.company;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random company names.
 */
public class CompanyNameSupplier extends RandomSupplier<String> {

    private static final String[] PREFIXES = {
            "Global", "National", "United", "International", "Universal", "Premier",
            "Advanced", "Digital", "Smart", "Tech", "Innovative", "Dynamic", "Strategic"
    };

    private static final String[] CORES = {
            "Solutions", "Systems", "Technologies", "Industries", "Enterprises", "Services",
            "Group", "Partners", "Consulting", "Corporation", "Holdings", "Ventures"
    };

    private static final String[] SUFFIXES = {
            "Inc.", "LLC", "Corp.", "Ltd.", "Co.", "Group", "International", "Worldwide"
    };

    @Override
    public String get() {
        int pattern = RANDOM.nextInt(3);
        return switch (pattern) {
            case 0 -> randomElement(PREFIXES) + " " + randomElement(CORES) + " " + randomElement(SUFFIXES);
            case 1 -> randomElement(PREFIXES) + " " + randomElement(CORES);
            default -> randomElement(CORES) + " " + randomElement(SUFFIXES);
        };
    }
}

