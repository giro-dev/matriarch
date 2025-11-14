package dev.agiro.matriarch.suppliers.personal;

import dev.agiro.matriarch.suppliers.base.ConfigurableSupplier;

import java.time.LocalDate;

/**
 * Supplier that generates random dates of birth.
 * Default range generates ages between 18-80 years.
 */
public class DateOfBirthSupplier extends ConfigurableSupplier<LocalDate> {

    private final int minAge;
    private final int maxAge;

    /**
     * Creates a supplier with default age range (18-80 years).
     */
    public DateOfBirthSupplier() {
        this(18, 80);
    }

    /**
     * Creates a supplier with custom age range.
     *
     * @param minAge minimum age in years
     * @param maxAge maximum age in years
     */
    public DateOfBirthSupplier(int minAge, int maxAge) {
        if (minAge < 0 || maxAge < minAge) {
            throw new IllegalArgumentException("Invalid age range");
        }
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    @Override
    public LocalDate get() {
        LocalDate now = LocalDate.now();
        int yearsAgo = randomInt(minAge, maxAge + 1);
        int daysOffset = RANDOM.nextInt(365);
        return now.minusYears(yearsAgo).minusDays(daysOffset);
    }
}

