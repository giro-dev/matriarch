package dev.agiro.matriarch.infrastructure;


import dev.agiro.matriarch.domain.model.KnownPatterns;
import dev.agiro.matriarch.domain.model.Pattern;
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
        patterns.getPatterns().add(new Pattern("email", "[a-z]{4,8}\\.[a-z]{4,8}_[a-z]{4,8}\\@(mercedes-benz|external-mercedes-benz).com",
                                            "regex"));
        patterns.getPatterns().add(new Pattern("partId", "A\\d{10}", "regex"));
        patterns.getPatterns().add(new Pattern("es1", "\\d{4}", "regex"));
        patterns.getPatterns().add(new Pattern("es2", "\\d{4}", "regex"));
        patterns.getPatterns().add(new Pattern("plantId", "\\d{4}", "regex"));
        patterns.getPatterns().add(new Pattern("supplier", "\\d{8}[A-Z]?", "regex"));
        patterns.getPatterns().add(new Pattern("plantType", "GLC,Other,Powertrain,MBCC,CKD,CBU,CKD,Cooperation,Remanufacturing,Other,VAN", "list"));
        patterns.getPatterns().add(new Pattern("contractType", "MBST-MEX,MBST-CHN,MTDP,MBST-EUR,MBST-USA", "list"));
    }

    public static KnownPatternsStore getInstance() {
        if (instance == null) {
            instance = new KnownPatternsStore();
        }
        return instance;
    }


}