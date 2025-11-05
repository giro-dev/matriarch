package dev.agiro.matriarch.domain.core;

import dev.agiro.matriarch.domain.model.Overrider;

/**
 * Strategy that sets excluded fields to null values.
 * This follows the Single Responsibility Principle by handling only
 * the logic for excluded fields.
 */
public class ExcludedFieldsStrategy implements OverrideStrategy {

    @Override
    public void applyOverrides(BuilderConfiguration<?> config, Class<?> clazz) {
        for (String excludedField : config.getExcludedFields()) {
            if (!config.hasOverride(excludedField)) {
                config.addOverride(excludedField, Overrider.nullValue());
            }
        }
    }
}

