package dev.agiro.matriarch.suppliers.numeric;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Supplier that generates random UUIDs.
 */
public class UuidSupplier implements Supplier<String> {

    @Override
    public String get() {
        return UUID.randomUUID().toString();
    }
}

