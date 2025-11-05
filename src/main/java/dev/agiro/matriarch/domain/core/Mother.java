package dev.agiro.matriarch.domain.core;

import dev.agiro.matriarch.domain.model.Overrider;
import dev.agiro.matriarch.domain.model.TypeReference;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
        private final Map<String, Overrider> overrides = new HashMap<>();
        private final Map<Class<?>, Supplier<?>> typeOverrides = new HashMap<>();
        private final Set<String> excludedFields = new HashSet<>();
        private final Mother<R> mother;
        private Integer collectionSizeMin = 1;
        private Integer collectionSizeMax = 15;
        private final TypeReference<R> typeReference;

        public Builder(Class<R> clazz) {
            this.mother = new Mother<>(clazz);
            this.typeReference = null;
        }

        public Builder(TypeReference<R> typeRef) {
            this.mother = new Mother<>(typeRef.getRawType());
            this.typeReference = typeRef;
        }

        /**
         * Override a specific field with a value.
         */
        public Builder<R> override(String key, Overrider value) {
            this.overrides.put(key, value);
            return this;
        }

        /**
         * Override a specific field with a string value.
         */
        public Builder<R> override(String key, String value) {
            this.overrides.put(key, null == value ? Overrider.nullValue() : Overrider.with(value));
            return this;
        }

        /**
         * Override a specific field with a regex pattern.
         */
        public Builder<R> override(String key, Regex value) {
            this.overrides.put(key, null == value ? Overrider.nullValue() : Overrider.regex(value.value()));
            return this;
        }

        /**
         * Override a specific field with an object value.
         */
        public Builder<R> override(String key, Object value) {
            this.overrides.put(key, null == value ? Overrider.nullValue() : Overrider.object(value));
            return this;
        }

        /**
         * Override a specific field with a value and type.
         */
        public Builder<R> override(String key, Object object, Overrider.OverriderType type) {
            if (object == null) {
                this.overrides.put(key, Overrider.nullValue());
                return this;
            }
            switch (type) {
                case STRING -> this.overrides.put(key, Overrider.with((String) object));
                case REGEX -> this.overrides.put(key, Overrider.regex((String) object));
                default -> this.overrides.put(key, Overrider.object(object));
            }
            return this;
        }

        /**
         * Set a custom generator for a specific field.
         * Usage: .forField("email", () -> faker.internet().emailAddress())
         */
        public Builder<R> forField(String fieldName, Supplier<?> generator) {
            this.overrides.put(fieldName, Overrider.supplier(generator));
            return this;
        }

        /**
         * Set a custom generator for all fields of a specific type.
         * Usage: .forType(LocalDate.class, () -> LocalDate.now().minusDays(random.nextInt(365)))
         */
        public <T> Builder<R> forType(Class<T> type, Supplier<T> generator) {
            this.typeOverrides.put(type, generator);
            return this;
        }

        /**
         * Exclude specific fields from generation.
         * Usage: .excludeFields("password", "internalId")
         */
        public Builder<R> excludeFields(String... fields) {
            this.excludedFields.addAll(Set.of(fields));
            // Set excluded fields to null
            for (String field : fields) {
                this.overrides.put(field, Overrider.nullValue());
            }
            return this;
        }

        /**
         * Set the size range for generated collections (Lists, Sets).
         * Usage: .withCollectionSize(5, 10)
         */
        public Builder<R> withCollectionSize(int min, int max) {
            if (min < 0 || max < min) {
                throw new IllegalArgumentException("Invalid collection size range: min=" + min + ", max=" + max);
            }
            this.collectionSizeMin = min;
            this.collectionSizeMax = max;
            // TODO: Pass these values to the generator
            return this;
        }

        /**
         * Build the object with the configured overrides.
         */
        public R build() {
            // Apply excluded fields as null overrides
            for (String excludedField : excludedFields) {
                if (!overrides.containsKey(excludedField)) {
                    overrides.put(excludedField, Overrider.nullValue());
                }
            }

            // Apply type overrides by finding all fields of matching types
            if (!typeOverrides.isEmpty()) {
                applyTypeOverrides(mother.clazz, "");
            }

            // If TypeReference was used, pass it to the generator for proper generic handling
            if (typeReference != null) {
                ObjectMotherGenerator generator = new ObjectMotherGenerator();
                return generator.createObject(typeReference, overrides);
            }

            return mother.create(overrides);
        }

        /**
         * Recursively apply type overrides to all fields of matching types.
         */
        private void applyTypeOverrides(Class<?> clazz, String prefix) {
            if (clazz == null || clazz == Object.class) {
                return;
            }

            for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                String fieldPath = prefix.isEmpty() ? field.getName() : prefix + "." + field.getName();

                // Skip if this field already has an explicit override
                if (overrides.containsKey(fieldPath)) {
                    continue;
                }

                // Check if any type override matches this field's type
                for (Map.Entry<Class<?>, Supplier<?>> typeOverride : typeOverrides.entrySet()) {
                    if (typeOverride.getKey().isAssignableFrom(field.getType())) {
                        overrides.put(fieldPath, Overrider.supplier(typeOverride.getValue()));
                        break;
                    }
                }
            }

            // Recurse to superclass
            applyTypeOverrides(clazz.getSuperclass(), prefix);
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
            if (count < 0) {
                throw new IllegalArgumentException("Count must be non-negative: " + count);
            }
            java.util.List<R> result = new java.util.ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                result.add(build());
            }
            return result;
        }

        /**
         * Generate multiple objects and return as a Set.
         * Usage: .buildSet(10)
         */
        public java.util.Set<R> buildSet(int count) {
            if (count < 0) {
                throw new IllegalArgumentException("Count must be non-negative: " + count);
            }
            java.util.Set<R> result = new java.util.HashSet<>(count);
            for (int i = 0; i < count; i++) {
                result.add(build());
            }
            return result;
        }

        /**
         * Generate multiple objects and return as a Stream.
         * Usage: .buildStream(10)
         */
        public java.util.stream.Stream<R> buildStream(int count) {
            if (count < 0) {
                throw new IllegalArgumentException("Count must be non-negative: " + count);
            }
            return java.util.stream.Stream.generate(this::build).limit(count);
        }

        /**
         * Generate an infinite Stream of objects.
         * Usage: .buildStream().limit(10)
         */
        public java.util.stream.Stream<R> buildStream() {
            return java.util.stream.Stream.generate(this::build);
        }

        /**
         * Get the collection size range.
         */
        public int getCollectionSizeMin() {
            return collectionSizeMin;
        }

        public int getCollectionSizeMax() {
            return collectionSizeMax;
        }
    }

    public record Regex(String value) {
    }
}