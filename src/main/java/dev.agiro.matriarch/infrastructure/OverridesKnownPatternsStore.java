package dev.agiro.matriarch.infrastructure;


import dev.agiro.matriarch.domain.model.KnownPatterns;
import dev.agiro.matriarch.domain.model.Pattern;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;

public class OverridesKnownPatternsStore {
    private static final String                      PATTERNS_FILE = "/patterns.yaml";
    private static       OverridesKnownPatternsStore instance;
    Logger logger = LoggerFactory.getLogger(OverridesKnownPatternsStore.class);

    private KnownPatterns knownPatterns = new KnownPatterns(new ArrayList<>());

    public KnownPatterns getKnownPatterns() {
        return knownPatterns;
    }

    public OverridesKnownPatternsStore() {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = getClass().getResourceAsStream(PATTERNS_FILE)) {
            if (inputStream == null) {
                logger.debug(() -> "YAML file not found: " + PATTERNS_FILE);
            } else {
                knownPatterns = yaml.loadAs(inputStream, KnownPatterns.class);
            }
        } catch (Exception e) {
            logger.error(e, () -> "Failed to load patterns.yaml YAML properties");
        }
        addStandardPatterns();
    }

    private void addStandardPatterns() {
        knownPatterns.patterns().add(new Pattern("email", "[a-z]{4,8}\\.[a-z]{4,8}_[a-z]{4,8}\\@(mercedes-benz|external-mercedes-benz).com",
                                                    "regex"));
        knownPatterns.patterns().add(new Pattern("partId", "A\\d{10}", "regex"));
        knownPatterns.patterns().add(new Pattern("es1", "\\d{4}", "regex"));
        knownPatterns.patterns().add(new Pattern("es2", "\\d{4}", "regex"));
        knownPatterns.patterns().add(new Pattern("plantId", "\\d{4}", "regex"));
        knownPatterns.patterns().add(new Pattern("supplier", "\\d{8}[A-Z]?", "regex"));
        knownPatterns.patterns().add(new Pattern("plantType", "GLC,Other,Powertrain,MBCC,CKD,CBU,CKD,Cooperation,Remanufacturing,Other,VAN", "list"));
        knownPatterns.patterns().add(new Pattern("contractType","MBST-MEX,MBST-CHN,MTDP,MBST-EUR,MBST-USA", "list"));
    }

    public static OverridesKnownPatternsStore getInstance() {
        if (instance == null) {
            instance = new OverridesKnownPatternsStore();
        }
        return instance;
    }


}