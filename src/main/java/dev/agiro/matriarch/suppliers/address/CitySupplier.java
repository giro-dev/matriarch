package dev.agiro.matriarch.suppliers.address;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random city names.
 */
public class CitySupplier extends RandomSupplier<String> {

    private static final String[] CITIES = {
            "New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio",
            "San Diego", "Dallas", "San Jose", "Austin", "Jacksonville", "Fort Worth", "Columbus",
            "Charlotte", "San Francisco", "Indianapolis", "Seattle", "Denver", "Washington",
            "Boston", "El Paso", "Nashville", "Detroit", "Oklahoma City", "Portland", "Las Vegas",
            "Memphis", "Louisville", "Baltimore", "Milwaukee", "Albuquerque", "Tucson", "Fresno",
            "Mesa", "Sacramento", "Atlanta", "Kansas City", "Colorado Springs", "Raleigh", "Miami",
            "Long Beach", "Virginia Beach", "Omaha", "Oakland", "Minneapolis", "Tulsa", "Tampa",
            "Barcelona", "Madrid", "València", "Sevilla", "Bilbao", "Málaga",
            "Paris", "Lyon", "Marseille", "Nice",
            "London", "Manchester", "Edinburgh",
            "Lisbon", "Porto",
            "Rome", "Milan", "Naples", "Venice", "Florence",
            "Berlin", "Munich", "Hamburg", "Cologne", "Frankfurt",
            "Amsterdam", "Rotterdam",
            "Brussels", "Antwerp",
            "Vienna", "Salzburg",
            "Prague", "Brno",
            "Warsaw", "Kraków",
            "Budapest",
            "Oslo", "Stockholm", "Helsinki", "Copenhagen",
            "Reykjavik"
    };

    @Override
    public String get() {
        return randomElement(CITIES);
    }
}

