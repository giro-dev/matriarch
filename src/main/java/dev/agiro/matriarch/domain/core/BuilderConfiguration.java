package dev.agiro.matriarch.domain.core;

import dev.agiro.matriarch.domain.model.Overrider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Configuration object for Builder that encapsulates all builder settings.
 * This follows the Single Responsibility Principle by separating configuration
 * from the building logic.
 */
public class BuilderConfiguration<R> {
    private final Map<String, Overrider> overrides = new HashMap<>();
    private final Map<Class<?>, Supplier<?>> typeOverrides = new HashMap<>();
    private final Set<String> excludedFields = new HashSet<>();
    private Integer collectionSizeMin = 1;
    private Integer collectionSizeMax = 15;

    public Map<String, Overrider> getOverrides() {
        return overrides;
    }

    public Map<Class<?>, Supplier<?>> getTypeOverrides() {
        return typeOverrides;
    }

    public Set<String> getExcludedFields() {
        return excludedFields;
    }

    public Integer getCollectionSizeMin() {
        return collectionSizeMin;
    }

    public Integer getCollectionSizeMax() {
        return collectionSizeMax;
    }

    public void setCollectionSizeMin(Integer collectionSizeMin) {
        this.collectionSizeMin = collectionSizeMin;
    }

    public void setCollectionSizeMax(Integer collectionSizeMax) {
        this.collectionSizeMax = collectionSizeMax;
    }

    public void addOverride(String key, Overrider value) {
        overrides.put(key, value);
    }

    public void addTypeOverride(Class<?> type, Supplier<?> generator) {
        typeOverrides.put(type, generator);
    }

    public void addExcludedFields(String... fields) {
        excludedFields.addAll(Set.of(fields));
    }

    public boolean hasOverride(String key) {
        return overrides.containsKey(key);
    }
}

