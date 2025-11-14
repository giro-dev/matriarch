package dev.agiro.matriarch.suppliers.company;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random industry names.
 */
public class IndustrySupplier extends RandomSupplier<String> {

    private static final String[] INDUSTRIES = {
            "Technology", "Software", "Information Technology", "Telecommunications",
            "Finance", "Banking", "Insurance", "Real Estate", "Healthcare", "Pharmaceuticals",
            "Biotechnology", "Manufacturing", "Automotive", "Aerospace", "Defense",
            "Energy", "Oil & Gas", "Renewable Energy", "Utilities", "Construction",
            "Retail", "E-commerce", "Consumer Goods", "Food & Beverage", "Hospitality",
            "Travel & Tourism", "Transportation", "Logistics", "Media", "Entertainment",
            "Publishing", "Education", "Consulting", "Professional Services", "Legal Services",
            "Marketing & Advertising", "Agriculture", "Mining", "Chemicals", "Textiles",
            "Electronics", "Telecommunications", "Gaming", "Sports", "Non-Profit"
    };

    @Override
    public String get() {
        return randomElement(INDUSTRIES);
    }
}

