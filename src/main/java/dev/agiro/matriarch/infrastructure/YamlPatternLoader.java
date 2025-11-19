package dev.agiro.matriarch.infrastructure;

import dev.agiro.matriarch.domain.model.KnownPatterns;
import dev.agiro.matriarch.domain.model.Pattern;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loads patterns from a YAML file in the classpath.
 * Single Responsibility: Only responsible for YAML file loading.
 */
public class YamlPatternLoader implements PatternLoader {
    private static final Logger logger = Logger.getLogger(YamlPatternLoader.class.getName());
    private final String resourcePath;

    public YamlPatternLoader(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    /**
     * Default constructor using standard patterns.yaml file.
     */
    public YamlPatternLoader() {
        this("/patterns.yaml");
    }

    @Override
    public List<Pattern> load() {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                logger.log(Level.WARNING, "YAML file not found: {0}", resourcePath);
                return new ArrayList<>();
            }

            KnownPatterns knownPatterns = yaml.loadAs(inputStream, KnownPatterns.class);
            if (knownPatterns == null || knownPatterns.getPatterns() == null) {
                logger.log(Level.WARNING, "No patterns found in YAML file: {0}", resourcePath);
                return new ArrayList<>();
            }

            logger.log(Level.FINE, "Loaded {0} patterns from {1}",
                    new Object[]{knownPatterns.getPatterns().size(), resourcePath});
            return new ArrayList<>(knownPatterns.getPatterns());

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load patterns from YAML file: " + resourcePath, e);
            return new ArrayList<>();
        }
    }

    @Override
    public int getPriority() {
        return 50; // Medium priority
    }
}

