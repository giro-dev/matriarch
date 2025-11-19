package dev.agiro.matriarch.suppliers.datetime;

import dev.agiro.matriarch.suppliers.base.ConfigurableSupplier;

import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Supplier that generates recent dates (java.util.Date).
 * Default range is within the last 7 days.
 */
public class RecentDateSupplier extends ConfigurableSupplier<Date> {

    private final int daysInPast;

    /**
     * Creates a supplier with default range (7 days in the past).
     */
    public RecentDateSupplier() {
        this(7);
    }

    /**
     * Creates a supplier with custom range.
     *
     * @param daysInPast maximum number of days in the past
     */
    public RecentDateSupplier(int daysInPast) {
        if (daysInPast < 0) {
            throw new IllegalArgumentException("daysInPast must be positive");
        }
        this.daysInPast = daysInPast;
    }

    @Override
    public Date get() {
        Instant now = Instant.now();
        long randomDays = RANDOM.nextInt(daysInPast + 1);
        Instant recent = now.minus(randomDays, ChronoUnit.DAYS);
        return Date.from(recent);
    }
}

