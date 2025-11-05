package dev.agiro.matriarch.infrastructure;

import dev.agiro.matriarch.domain.model.Pattern;

import java.util.List;

/**
 * Strategy interface for loading patterns from different sources.
 * Follows Open/Closed Principle - open for extension, closed for modification.
 */
public interface PatternLoader {
    /**
     * Load patterns from the source.
     *
     * @return List of patterns loaded from this source
     */
    List<Pattern> load();

    /**
     * Get the priority of this loader (lower number = higher priority).
     * Allows multiple loaders to be combined with defined precedence.
     *
     * @return priority value
     */
    default int getPriority() {
        return 100;
    }
}

