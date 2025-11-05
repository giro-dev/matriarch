package dev.agiro.matriarch.domain.core;

import dev.agiro.matriarch.domain.model.Overrider;
import dev.agiro.matriarch.domain.model.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Mother<M> {
    private final ObjectMotherGenerator objectMotherGenerator = new ObjectMotherGenerator();
    private final Class<M> clazz;

    public Mother(Class<M> clazz) {
        this.clazz = clazz;
    }

    public M create() {
        return objectMotherGenerator.createObject(clazz);
    }
    public M create(Map<String, Overrider> overrideValues) {
        return objectMotherGenerator.createObject(clazz, overrideValues);
    }

    public static <R> Builder<R> forClass(Class<R> clazz) {
        return new Builder<>(clazz);
    }

    /**
     * Create a builder with TypeReference support for generic types.
     * Usage: Mother.forType(new TypeReference<List<String>>() {}).build();
     */
    public static <R> Builder<R> forType(TypeReference<R> typeRef) {
        return new Builder<>(typeRef);
    }

    public static class Builder<R> {
        private final Mother<R> mother;
        private final BuilderConfiguration<R> config;
        private final List<OverrideStrategy> strategies;
        private final CollectionGenerator<R> collectionGenerator;
        private final TypeReference<R> typeReference;

        public Builder(Class<R> clazz) {
            this.mother = new Mother<>(clazz);
            this.config = new BuilderConfiguration<>();
            this.strategies = new ArrayList<>();
            this.collectionGenerator = new CollectionGenerator<>(this::build);
            this.typeReference = null;
            initializeStrategies();
        }

        public Builder(TypeReference<R> typeRef) {
            this.mother = new Mother<>(typeRef.getRawType());
            this.config = new BuilderConfiguration<>();
            this.strategies = new ArrayList<>();
            this.collectionGenerator = new CollectionGenerator<>(this::build);
            this.typeReference = typeRef;
            initializeStrategies();
        }

        /**
         * Initialize the override strategies that will be applied during build.
         */
        private void initializeStrategies() {
            strategies.add(new ExcludedFieldsStrategy());
            strategies.add(new TypeOverrideStrategy());
        }

        /**
         * Override a specific field with a value.
         */
        public Builder<R> override(String key, Overrider value) {
            return forField(key, value);
        }

        /**
         * Override a specific field with a string value.
         */
        public Builder<R> override(String key, String value) {
            return forField(key, value);
        }

        /**
         * Override a specific field with a regex pattern.
         */
        public Builder<R> override(String key, Regex value) {
            return forField(key, value);
        }

        /**
         * Override a specific field with an object value.
         */
        public Builder<R> override(String key, Object value) {
            return forField(key, value);
        }

        /**
         * Override a specific field with a value and type.
         */
        public Builder<R> override(String key, Object object, Overrider.OverriderType type) {
            return forField(key, object, type);

        }

        /**
         * Override a specific field with a value.
         */
        public Builder<R> forField(String key, Overrider value) {
            config.addOverride(key, value);
            return this;
        }

        /**
         * Override a specific field with a string value.
         */
        public Builder<R> forField(String key, String value) {
            config.addOverride(key, value == null ? Overrider.nullValue() : Overrider.with(value));
            return this;
        }

        /**
         * Override a specific field with a regex pattern.
         */
        public Builder<R> forField(String key, Regex value) {
            config.addOverride(key, value == null ? Overrider.nullValue() : Overrider.regex(value.value()));
            return this;
        }

        /**
         * Override a specific field with an object value.
         */
        public Builder<R> forField(String key, Object value) {
            config.addOverride(key, value == null ? Overrider.nullValue() : Overrider.object(value));
            return this;
        }

        /**
         * Override a specific field with a value and type.
         */
        public Builder<R> forField(String key, Object object, Overrider.OverriderType type) {
            if (object == null) {
                config.addOverride(key, Overrider.nullValue());
                return this;
            }
            switch (type) {
                case STRING -> config.addOverride(key, Overrider.with((String) object));
                case REGEX -> config.addOverride(key, Overrider.regex((String) object));
                default -> config.addOverride(key, Overrider.object(object));
            }
            return this;
        }

        /**
         * Set a custom generator for a specific field.
         * Usage: .forField("email", () -> faker.internet().emailAddress())
         */
        public Builder<R> forField(String fieldName, Supplier<?> generator) {
            config.addOverride(fieldName, Overrider.supplier(generator));
            return this;
        }

        /**
         * Set a custom generator for all fields of a specific type.
         * Usage: .forType(LocalDate.class, () -> LocalDate.now().minusDays(random.nextInt(365)))
         */
        public <T> Builder<R> forType(Class<T> type, Supplier<T> generator) {
            config.addTypeOverride(type, generator);
            return this;
        }

        /**
         * Exclude specific fields from generation.
         * Usage: .excludeFields("password", "internalId")
         */
        public Builder<R> excludeFields(String... fields) {
            config.addExcludedFields(fields);
            return this;
        }

        /**
         * Set the size range for generated collections (Lists, Sets).
         * Usage: .withCollectionSize(5, 10)
         * Note: The configuration is stored, but currently not applied during object generation.
         */
        public Builder<R> withCollectionSize(int min, int max) {
            if (min < 0 || max < min) {
                throw new IllegalArgumentException("Invalid collection size range: min=" + min + ", max=" + max);
            }
            config.setCollectionSizeMin(min);
            config.setCollectionSizeMax(max);
            return this;
        }

        /**
         * Build the object with the configured overrides.
         */
        public R build() {
            // Apply all override strategies
            strategies.forEach(strategy -> strategy.applyOverrides(config, mother.clazz));

            // If TypeReference was used, pass it to the generator for proper generic handling
            if (typeReference != null) {
                ObjectMotherGenerator generator = new ObjectMotherGenerator();
                return generator.createObject(typeReference, config.getOverrides());
            }

            return mother.create(config.getOverrides());
        }

        /**
         * Alias for build() - more semantic name for object creation.
         */
        public R birth() {
            return build();
        }

        /**
         * Generate multiple objects and return as a List.
         * Usage: .buildList(10)
         */
        public java.util.List<R> buildList(int count) {
            return collectionGenerator.generateList(count);
        }

        /**
         * Generate multiple objects and return as a Set.
         * Usage: .buildSet(10)
         */
        public java.util.Set<R> buildSet(int count) {
            return collectionGenerator.generateSet(count);
        }

        /**
         * Generate multiple objects and return as a Stream.
         * Usage: .buildStream(10)
         */
        public java.util.stream.Stream<R> buildStream(int count) {
            return collectionGenerator.generateStream(count);
        }

        /**
         * Generate an infinite Stream of objects.
         * Usage: .buildStream().limit(10)
         */
        public java.util.stream.Stream<R> buildStream() {
            return collectionGenerator.generateInfiniteStream();
        }

        /**
         * Get the collection size range.
         */
        public int getCollectionSizeMin() {
            return config.getCollectionSizeMin();
        }

        public int getCollectionSizeMax() {
            return config.getCollectionSizeMax();
        }
    }
}