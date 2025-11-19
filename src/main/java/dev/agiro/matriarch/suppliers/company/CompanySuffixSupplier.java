package dev.agiro.matriarch.suppliers.company;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random company suffixes.
 */
public class CompanySuffixSupplier extends RandomSupplier<String> {

    private static final String[] SUFFIXES = {
            "Inc.", "LLC", "Corp.", "Corporation", "Ltd.", "Limited", "Co.", "Company",
            "Group", "International", "Worldwide", "Global", "Partners", "Associates",
            "Holdings", "Enterprises", "Industries", "Solutions", "Services", "Systems"
    };

    @Override
    public String get() {
        return randomElement(SUFFIXES);
    }
}

