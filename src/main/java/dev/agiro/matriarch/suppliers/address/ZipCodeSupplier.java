package dev.agiro.matriarch.suppliers.address;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random ZIP/postal codes.
 * Default format is US ZIP code (5 digits or ZIP+4).
 */
public class ZipCodeSupplier extends RandomSupplier<String> {


    private final Format format;

    private ZipCodeSupplier(Format format) {
        this.format = format;
    }

    public ZipCodeSupplier() {
        this.format = Format.FIVE_DIGIT;
    }

    public static ZipCodeSupplier fiveDigit() {
        return new ZipCodeSupplier(Format.FIVE_DIGIT);
    }

    public static ZipCodeSupplier zipPlusFour() {
        return new ZipCodeSupplier(Format.ZIP_PLUS_FOUR);
    }

    @Override
    public String get() {
        return switch (format) {
            case FIVE_DIGIT -> String.format("%05d", RANDOM.nextInt(100000));
            case ZIP_PLUS_FOUR -> String.format("%05d-%04d", RANDOM.nextInt(100000), RANDOM.nextInt(10000));
        };
    }

    enum Format {
        FIVE_DIGIT,
        ZIP_PLUS_FOUR;
    }
}

