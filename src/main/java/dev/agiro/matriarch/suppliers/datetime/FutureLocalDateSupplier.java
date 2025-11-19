package dev.agiro.matriarch.suppliers.datetime;

import dev.agiro.matriarch.suppliers.base.ConfigurableSupplier;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * Supplier that generates random future LocalDate values.
 * Default range is within the next 365 days.
 */
public class FutureLocalDateSupplier extends ConfigurableSupplier<LocalDate> {

    private final int amountInFuture;
    private final TemporalUnit temporalUnit;


    public FutureLocalDateSupplier(int amountInFuture, TemporalUnit temporalUnit) {
        if (amountInFuture < 0) {
            throw new IllegalArgumentException("daysInFuture must be positive");
        }
        this.amountInFuture = amountInFuture;
        this.temporalUnit = temporalUnit;

    }

    /**
     * Creates a supplier with default range (365 days in the future).
     */
    public FutureLocalDateSupplier() {
        this(365, ChronoUnit.DAYS);
    }

    /**
     * Creates a supplier with custom range.
     *
     * @param amountInFuture maximum number of days in the future
     */
    public FutureLocalDateSupplier(int amountInFuture) {
        this(amountInFuture, ChronoUnit.DAYS);
    }


    @Override
    public LocalDate get() {
        int randomDays = RANDOM.nextInt(amountInFuture + 1);
        return LocalDate.now().plus(randomDays, temporalUnit);
    }
}

