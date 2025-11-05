package dev.agiro.matriarch.infrastructure;

import dev.agiro.matriarch.domain.model.KnownPatterns;
import dev.agiro.matriarch.domain.model.Pattern;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Composite implementation of PatternRepository that delegates to multiple PatternLoaders.
 * Follows Single Responsibility and Open/Closed Principles.
 * Thread-safe implementation.
 */
public class CompositePatternRepository implements PatternRepository {
    private static final Logger logger = Logger.getLogger(CompositePatternRepository.class.getName());

    private final List<PatternLoader> loaders;
    private volatile KnownPatterns cachedPatterns;
    private final Object lock = new Object();

    /**
     * Create repository with custom loaders.
     *
     * @param loaders list of pattern loaders to use
     */
    public CompositePatternRepository(List<PatternLoader> loaders) {
        if (loaders == null || loaders.isEmpty()) {
            throw new IllegalArgumentException("At least one PatternLoader must be provided");
        }
        // Sort by priority (lower number = higher priority)
        this.loaders = loaders.stream()
                .sorted(Comparator.comparingInt(PatternLoader::getPriority))
                .collect(Collectors.toList());
    }

    /**
     * Create repository with default loaders (Standard + YAML).
     */
    public CompositePatternRepository() {
        this(Arrays.asList(
                new SystemPropertyPatternLoader(),
                new StandardPatternLoader(),
                new YamlPatternLoader()
        ));
    }

    @Override
    public KnownPatterns loadPatterns() {
        Map<String, Pattern> patternMap = new LinkedHashMap<>();

        // Load patterns from all loaders in priority order
        // Higher priority loaders can override patterns from lower priority ones
        for (PatternLoader loader : loaders) {
            try {
                List<Pattern> patterns = loader.load();
                for (Pattern pattern : patterns) {
                    if (pattern.getCoordinate() != null) {
                        patternMap.put(pattern.getCoordinate(), pattern);
                    }
                }
                logger.log(Level.FINE, "Loaded patterns from {0}", loader.getClass().getSimpleName());
            } catch (Exception e) {
                logger.log(Level.WARNING, "Failed to load patterns from " + loader.getClass().getSimpleName(), e);
            }
        }

        List<Pattern> allPatterns = new ArrayList<>(patternMap.values());
        logger.log(Level.INFO, "Total patterns loaded: {0}", allPatterns.size());

        return new KnownPatterns(allPatterns);
    }

    @Override
    public KnownPatterns getPatterns() {
        if (cachedPatterns == null) {
            synchronized (lock) {
                if (cachedPatterns == null) {
                    cachedPatterns = loadPatterns();
                }
            }
        }
        return cachedPatterns;
    }

    @Override
    public void reload() {
        synchronized (lock) {
            logger.log(Level.INFO, "Reloading patterns from all sources");
            cachedPatterns = loadPatterns();
        }
    }

    /**
     * Get the list of loaders being used by this repository.
     *
     * @return unmodifiable list of loaders
     */
    public List<PatternLoader> getLoaders() {
        return Collections.unmodifiableList(loaders);
    }
}

