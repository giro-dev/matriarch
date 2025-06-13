package dev.agiro.matriarch.infrastructure;


import dev.agiro.matriarch.domain.model.KnownPatterns;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;

public class KnownPatternsStore {
    private static final String             PATTERNS_FILE = "/patterns.yaml";
    private static       KnownPatternsStore instance;
    Logger logger = LoggerFactory.getLogger(KnownPatternsStore.class);

    private KnownPatterns patterns = new KnownPatterns(new ArrayList<>());

    public KnownPatterns getPatterns() {
        return patterns;
    }

    public KnownPatternsStore() {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = getClass().getResourceAsStream(PATTERNS_FILE)) {
            if (inputStream == null) {
                logger.debug(() -> "YAML file not found: " + PATTERNS_FILE);
            } else {
                patterns = yaml.loadAs(inputStream, KnownPatterns.class);
            }
        } catch (Exception e) {
            logger.error(e, () -> "Failed to load patterns.yaml YAML properties");
        }
        addStandardPatterns();
    }

    private void addStandardPatterns() {

    }

    public static KnownPatternsStore getInstance() {
        if (instance == null) {
            instance = new KnownPatternsStore();
        }
        return instance;
    }


}