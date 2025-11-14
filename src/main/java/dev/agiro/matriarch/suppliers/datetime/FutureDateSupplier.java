package dev.agiro.matriarch.suppliers.datetime;

import dev.agiro.matriarch.suppliers.base.ConfigurableSupplier;

import java.time.LocalDate;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Supplier that generates random future dates (java.util.Date).
 * Default range is within the next 365 days.
 */
public class FutureDateSupplier extends ConfigurableSupplier<Date> {

    private final int amountInFuture;
    private final TemporalUnit temporalUnit;

    public FutureDateSupplier(int amountInFuture, TemporalUnit temporalUnit) {
        if (amountInFuture < 0) {
            throw new IllegalArgumentException("daysInFuture must be positive");
        }
        this.amountInFuture = amountInFuture;
        this.temporalUnit = temporalUnit;
    }

    /**
     * Creates a supplier with default range (365 days in the future).
     */
    public FutureDateSupplier() {
        this(365, ChronoUnit.DAYS);
    }

    /**
     * Creates a supplier with custom range.
     *
     * @param amountInFuture maximum number of days in the future
     */
    public FutureDateSupplier(int amountInFuture) {
        this(amountInFuture, ChronoUnit.DAYS);
    }

    @Override
    public Date get() {
        long randomAmount = RANDOM.nextInt(amountInFuture + 1);
        LocalDate now = LocalDate.now().plus(randomAmount, temporalUnit);
        Instant future = now.atTime(12, 0).toInstant(java.time.ZoneOffset.UTC);
        return Date.from(future);
    }
}

