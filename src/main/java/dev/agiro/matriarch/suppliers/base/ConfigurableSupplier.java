package dev.agiro.matriarch.suppliers.base;

/**
 * Base class for suppliers that can be configured with parameters.
 * Subclasses should provide constructors that accept configuration parameters.
 */
public abstract class ConfigurableSupplier<T> extends RandomSupplier<T> {

    /**
     * Creates a new instance with default configuration.
     */
    protected ConfigurableSupplier() {
    }
}

