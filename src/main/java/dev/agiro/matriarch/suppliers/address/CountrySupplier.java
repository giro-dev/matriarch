package dev.agiro.matriarch.suppliers.address;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random country names.
 */
public class CountrySupplier extends RandomSupplier<String> {

    private static final String[] COUNTRIES = {
            "United States", "Canada", "United Kingdom", "Germany", "France", "Italy", "Spain",
            "Netherlands", "Belgium", "Switzerland", "Austria", "Sweden", "Norway", "Denmark",
            "Finland", "Poland", "Czech Republic", "Portugal", "Greece", "Ireland", "Australia",
            "New Zealand", "Japan", "South Korea", "China", "India", "Brazil", "Mexico",
            "Argentina", "Chile", "Colombia", "Peru", "South Africa", "Egypt", "Turkey",
            "Israel", "United Arab Emirates", "Saudi Arabia", "Singapore", "Malaysia", "Thailand",
            "Indonesia", "Philippines", "Vietnam", "Russia", "Ukraine", "Romania", "Hungary"
    };

    @Override
    public String get() {
        return randomElement(COUNTRIES);
    }
}

