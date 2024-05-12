package dev.agiro.matriarch.application;




import dev.agiro.matriarch.model.Overrider;

import java.util.HashMap;
import java.util.Map;

public class Mother<M> {
    private final ObjectMotherGenerator objectMotherGenerator = new ObjectMotherGenerator();
    private final Class<M> clazz;

    public Mother(Class<M> clazz) {
        this.clazz = clazz;
    }

    public M create() {
        return objectMotherGenerator.create(clazz);
    }
    public M create(Map<String, Overrider> overrideValues) {
        return objectMotherGenerator.create(clazz, overrideValues);
    }

    public static <R> Builder<R> forObject(Class<R> clazz) {
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

        public R create() {
            return mother.create( overrides);
        }
    }
}