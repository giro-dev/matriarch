package dev.agiro.matriarch.infrastructure;

import dev.agiro.matriarch.domain.model.Pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loads standard/built-in patterns that are always available.
 * These patterns provide sensible defaults for common field names.
 * Single Responsibility: Only responsible for providing built-in patterns.
 */
public class StandardPatternLoader implements PatternLoader {
    private static final Logger logger = Logger.getLogger(StandardPatternLoader.class.getName());

    @Override
    public List<Pattern> load() {
        List<Pattern> patterns = new ArrayList<>();

        // Email patterns
        patterns.add(new Pattern("email", "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}", "regex"));
        patterns.add(new Pattern("emailAddress", "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}", "regex"));

        // Phone patterns
        patterns.add(new Pattern("phone", "\\+?[0-9]{10,14}", "regex"));
        patterns.add(new Pattern("phoneNumber", "\\+?[0-9]{10,14}", "regex"));

        // ID patterns
        patterns.add(new Pattern("id", "[a-zA-Z0-9]{8,32}", "regex"));
        patterns.add(new Pattern("identifier", "[a-zA-Z0-9]{8,32}", "regex"));

        // Name patterns
        patterns.add(new Pattern("firstName", "[A-Z][a-z]{2,15}", "regex"));
        patterns.add(new Pattern("lastName", "[A-Z][a-z]{2,20}", "regex"));

        // Address patterns
        patterns.add(new Pattern("zipCode", "[0-9]{5}", "regex"));
        patterns.add(new Pattern("postalCode", "[0-9]{5}", "regex"));

        // Status patterns
        patterns.add(new Pattern("status", "ACTIVE,INACTIVE,PENDING,COMPLETED", "list"));
        patterns.add(new Pattern("priority", "LOW,MEDIUM,HIGH,CRITICAL", "list"));

        logger.log(Level.INFO, "Loaded {0} standard patterns", patterns.size());
        return patterns;
    }

    @Override
    public int getPriority() {
        return 100; // Lowest priority - can be overridden by YAML
    }
}

