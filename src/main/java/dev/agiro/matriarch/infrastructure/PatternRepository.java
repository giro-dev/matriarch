package dev.agiro.matriarch.infrastructure;

import dev.agiro.matriarch.domain.model.KnownPatterns;

/**
 * Interface for loading and managing known patterns.
 * Follows Dependency Inversion Principle - depend on abstractions, not concretions.
 */
public interface PatternRepository {
    /**
     * Load patterns from the configured source.
     *
     * @return KnownPatterns containing all loaded patterns
     */
    KnownPatterns loadPatterns();

    /**
     * Get the currently loaded patterns.
     *
     * @return KnownPatterns containing all patterns
     */
    KnownPatterns getPatterns();

    /**
     * Reload patterns from source.
     */
    void reload();
}

