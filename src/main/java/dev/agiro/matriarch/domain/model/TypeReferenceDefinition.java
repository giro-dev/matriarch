package dev.agiro.matriarch.domain.model;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Definition for objects created using TypeReference.
 * This allows proper handling of generic types like List<String>.
 */
public record TypeReferenceDefinition<T>(
        TypeReference<T> typeReference,
        Map<String, Overrider> overrideValues,
        String overrideCoordinate) implements Definition {
    
    @Override
    public Class<?> clazz() {
        return typeReference.getRawType();
    }
    
    @Override
    public Type[] parametrizedType() {
        return typeReference.getActualTypeArguments();
    }
}

