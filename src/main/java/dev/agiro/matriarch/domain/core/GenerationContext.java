package dev.agiro.matriarch.domain.core;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Thread-local context that carries configuration through the generation pipeline.
 * Follows the same ThreadLocal pattern as {@link CircularDependencyDetector}.
 */
public class GenerationContext {

    private static final GenerationContext INSTANCE = new GenerationContext();

    private final ThreadLocal<Config> currentConfig = ThreadLocal.withInitial(Config::defaults);

    private GenerationContext() {}

    public static GenerationContext getInstance() {
        return INSTANCE;
    }

    public void set(Config config) {
        currentConfig.set(config);
    }

    public void clear() {
        currentConfig.remove();
    }

    public Random getRandom() {
        return currentConfig.get().random;
    }

    public int getCollectionSizeMin() {
        return currentConfig.get().collectionSizeMin;
    }

    public int getCollectionSizeMax() {
        return currentConfig.get().collectionSizeMax;
    }

    public boolean isStrictMode() {
        return currentConfig.get().strictMode;
    }

    public boolean isDebugMode() {
        return currentConfig.get().debugMode;
    }

    /**
     * Generate a random collection size within the configured range.
     */
    public int randomCollectionSize() {
        int min = getCollectionSizeMin();
        int max = getCollectionSizeMax();
        if (min == max) return min;
        return min + getRandom().nextInt(max - min + 1);
    }

    public static class Config {
        private final Random random;
        private final int collectionSizeMin;
        private final int collectionSizeMax;
        private final boolean strictMode;
        private final boolean debugMode;

        private Config(Random random, int collectionSizeMin, int collectionSizeMax,
                       boolean strictMode, boolean debugMode) {
            this.random = random;
            this.collectionSizeMin = collectionSizeMin;
            this.collectionSizeMax = collectionSizeMax;
            this.strictMode = strictMode;
            this.debugMode = debugMode;
        }

        public static Config defaults() {
            return new Config(new SecureRandom(), 1, 15, false, false);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Random random = new SecureRandom();
            private int collectionSizeMin = 1;
            private int collectionSizeMax = 15;
            private boolean strictMode = false;
            private boolean debugMode = false;

            public Builder random(Random random) {
                this.random = random;
                return this;
            }

            public Builder collectionSizeMin(int min) {
                this.collectionSizeMin = min;
                return this;
            }

            public Builder collectionSizeMax(int max) {
                this.collectionSizeMax = max;
                return this;
            }

            public Builder strictMode(boolean strict) {
                this.strictMode = strict;
                return this;
            }

            public Builder debugMode(boolean debug) {
                this.debugMode = debug;
                return this;
            }

            public Config build() {
                return new Config(random, collectionSizeMin, collectionSizeMax, strictMode, debugMode);
            }
        }
    }
}
