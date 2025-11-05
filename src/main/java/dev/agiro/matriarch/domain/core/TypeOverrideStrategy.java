package dev.agiro.matriarch.domain.core;

import dev.agiro.matriarch.domain.model.Overrider;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Strategy that applies type-based overrides to all fields matching specific types.
 * This follows the Single Responsibility Principle by handling only
 * the logic for type-based overrides.
 */
public class TypeOverrideStrategy implements OverrideStrategy {

    @Override
    public void applyOverrides(BuilderConfiguration<?> config, Class<?> clazz) {
        if (config.getTypeOverrides().isEmpty()) {
            return;
        }
        applyTypeOverridesRecursive(config, clazz, "");
    }

    /**
     * Recursively apply type overrides to all fields of matching types.
     */
    private void applyTypeOverridesRecursive(BuilderConfiguration<?> config, Class<?> clazz, String prefix) {
        if (clazz == null || clazz == Object.class) {
            return;
        }

        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            String fieldPath = prefix.isEmpty() ? field.getName() : prefix + "." + field.getName();

            // Skip if this field already has an explicit override
            if (config.hasOverride(fieldPath)) {
                continue;
            }

            // Check if any type override matches this field's type
            for (Map.Entry<Class<?>, Supplier<?>> typeOverride : config.getTypeOverrides().entrySet()) {
                if (typeOverride.getKey().isAssignableFrom(field.getType())) {
                    config.addOverride(fieldPath, Overrider.supplier(typeOverride.getValue()));
                    break;
                }
            }
        }

        // Recurse to superclass
        applyTypeOverridesRecursive(config, clazz.getSuperclass(), prefix);
    }
}

