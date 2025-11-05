package dev.agiro.matriarch.domain.core;

/**
 * Strategy interface for applying different types of overrides to builder configuration.
 * This follows the Strategy Pattern and Open/Closed Principle, allowing new override
 * strategies to be added without modifying existing code.
 */
public interface OverrideStrategy {
    /**
     * Apply this strategy's overrides to the configuration.
     *
     * @param config the builder configuration to modify
     * @param clazz the class being built
     */
    void applyOverrides(BuilderConfiguration<?> config, Class<?> clazz);
}

