package dev.agiro.matriarch.suppliers.address;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random street addresses.
 */
public class StreetAddressSupplier extends RandomSupplier<String> {

    private static final String[] STREET_NAMES = {
            "Main", "Oak", "Maple", "Cedar", "Elm", "Washington", "Lake", "Hill",
            "Park", "River", "Church", "Pine", "Spring", "Walnut", "Forest", "Sunset",
            "Highland", "Madison", "Jackson", "Lincoln", "Franklin", "Jefferson", "Adams",
            "Wilson", "Roosevelt", "Kennedy", "Broadway", "First", "Second", "Third"
    };

    private static final String[] STREET_TYPES = {
            "Street", "Avenue", "Boulevard", "Road", "Drive", "Lane", "Way",
            "Court", "Place", "Terrace", "Circle", "Parkway"
    };

    @Override
    public String get() {
        int number = randomInt(1, 9999);
        String streetName = randomElement(STREET_NAMES);
        String streetType = randomElement(STREET_TYPES);

        return number + " " + streetName + " " + streetType;
    }
}

