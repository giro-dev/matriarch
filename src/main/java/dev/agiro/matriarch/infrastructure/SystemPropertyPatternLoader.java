package dev.agiro.matriarch.infrastructure;

import dev.agiro.matriarch.domain.model.Pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example loader that loads patterns from system properties or environment variables.
 * Demonstrates extensibility of the pattern loading system.
 * System properties format:
 * -Dmatriarch.pattern.{coordinate}={type}:{value}
 * Example:
 * -Dmatriarch.pattern.customEmail=regex:[a-z]+@custom\\.com
 * -Dmatriarch.pattern.environment=list:DEV,TEST,PROD
 */
public class SystemPropertyPatternLoader implements PatternLoader {
    private static final Logger logger = Logger.getLogger(SystemPropertyPatternLoader.class.getName());
    private static final String PROPERTY_PREFIX = "matriarch.pattern.";

    @Override
    public List<Pattern> load() {
        List<Pattern> patterns = new ArrayList<>();
        Properties props = System.getProperties();

        for (String propertyName : props.stringPropertyNames()) {
            if (propertyName.startsWith(PROPERTY_PREFIX)) {
                String coordinate = propertyName.substring(PROPERTY_PREFIX.length());
                String value = props.getProperty(propertyName);
                
                Pattern pattern = parsePattern(coordinate, value);
                if (pattern != null) {
                    patterns.add(pattern);
                }
            }
        }

        if (!patterns.isEmpty()) {
            logger.log(Level.FINE, "Loaded {0} patterns from system properties", patterns.size());
        }

        return patterns;
    }

    /**
     * Parse pattern from system property value.
     * Format: {type}:{value} or just {value} (defaults to regex)
     */
    private Pattern parsePattern(String coordinate, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String type = "regex";
        String patternValue = value;

        int colonIndex = value.indexOf(':');
        if (colonIndex > 0) {
            String potentialType = value.substring(0, colonIndex).trim().toLowerCase();
            if (potentialType.equals("regex") || potentialType.equals("string") || potentialType.equals("list")) {
                type = potentialType;
                patternValue = value.substring(colonIndex + 1).trim();
            }
        }

        return new Pattern(coordinate, patternValue, type);
    }

    @Override
    public int getPriority() {
        return 10; // Highest priority - system properties override everything
    }
}

