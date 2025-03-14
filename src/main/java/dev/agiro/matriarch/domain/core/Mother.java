package dev.agiro.matriarch.domain.core;

import dev.agiro.matriarch.domain.model.Overrider;

import java.util.HashMap;
import java.util.Map;

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

    public static class Builder<R> {
        private final Map<String, Overrider> overrides = new HashMap<>();
        private final Mother<R> mother;

        public Builder(Class<R> clazz) {
            this.mother = new Mother<>(clazz);
        }

        public Builder<R> override(String key, Overrider value) {
            this.overrides.put(key, value);
            return this;
        }

        public Builder<R> override(String key, String value) {
            this.overrides.put(key, null == value ? Overrider.nullValue() : Overrider.with(value));
            return this;
        }

        public Builder<R> override(String key, Regex value) {
            this.overrides.put(key, null == value ? Overrider.nullValue() : Overrider.regex(value.value()));
            return this;
        }

        public Builder<R> override(String key, Object value) {
            this.overrides.put(key, null == value ? Overrider.nullValue() : Overrider.object(value));
            return this;
        }

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
        public R build() {
            return mother.create( overrides);
        }
        public R birth() {
            return mother.create( overrides);
        }
    }

    public record Regex(String value) {
    }
}