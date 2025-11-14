package dev.agiro.matriarch.junit.annotations.internal;

import java.util.function.Supplier;

/**
 * Marker type used as the default for @OverrideField.supplier().
 */
public final class NoSupplier implements Supplier<Object> {
    @Override
    public Object get() {
        throw new UnsupportedOperationException("NoSupplier is a marker and should not be instantiated");
    }
}

