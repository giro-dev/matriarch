package dev.agiro.matriarch.suppliers.datetime;

import dev.agiro.matriarch.suppliers.base.ConfigurableSupplier;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * Supplier that generates random past dates.
 * Default range is within the last 365 days.
 */
public class PastLocalDateSupplier extends ConfigurableSupplier<LocalDate> {

    private final int amountInPast;
    private final TemporalUnit temporalUnit;

    /**
     * Creates a supplier with default range (365 days in the past).
     */
    public PastLocalDateSupplier() {
        this(ChronoUnit.DAYS, 365);
    }

    /**
     * Creates a supplier with custom range.
     *
     * @param daysInPast maximum number of days in the past
     */
    public PastLocalDateSupplier(int daysInPast) {
        this(ChronoUnit.DAYS, daysInPast);
    }

    /**
     * Generates a random past date within the configured range.
     *
     * @param temporalUnit adjuster
     * @param amountInPast to adjust to the past
     */
    public PastLocalDateSupplier(TemporalUnit temporalUnit, int amountInPast) {
        if (amountInPast < 0) {
            throw new IllegalArgumentException("amountInPast must be positive");
        }
        this.amountInPast = amountInPast;
        this.temporalUnit = temporalUnit;
    }

    @Override
    public LocalDate get() {
        int randomAmount = RANDOM.nextInt(amountInPast + 1);
        return LocalDate.now().minus(randomAmount, temporalUnit);
    }
}

