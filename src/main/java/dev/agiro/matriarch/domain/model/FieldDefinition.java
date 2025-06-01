package dev.agiro.matriarch.domain.model;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays; // Import Arrays
import java.util.Map;

public record FieldDefinition(Field field,
                              Map<String, Overrider> overrideValues,
                              String overrideCoordinate,
                              Map<java.lang.reflect.TypeVariable<?>, Type> resolvedGenericTypeMap) implements Definition {

    public FieldDefinition(Field field, Map<String, Overrider> overrideValues, String overrideCoordinate) {
        this(field, overrideValues, overrideCoordinate, java.util.Collections.emptyMap());
    }

    @Override
    public Class<?> clazz() {
        Type fieldType = field.getGenericType();
        if (fieldType instanceof java.lang.reflect.TypeVariable<?> typeVariable) {
            Type resolvedType = resolvedGenericTypeMap().get(typeVariable);
            if (resolvedType instanceof Class<?> resolvedClass) {
                return resolvedClass;
            } else if (resolvedType instanceof java.lang.reflect.ParameterizedType parameterizedResolvedType) {
                 // It's a ParameterizedType like List<String>, return the raw type (List.class)
                return (Class<?>) parameterizedResolvedType.getRawType();
            }
            // Fallback or handle cases where type variable is not in map or not a Class
            // This might happen if the type variable couldn't be resolved from the parent context
        }
        return field.getType(); // Returns the raw type if not a resolvable TypeVariable
    }

    @Override
    public Type[] parametrizedType() {
        Type genericType = field.getGenericType();

        if (genericType instanceof java.lang.reflect.ParameterizedType parameterizedType) {
            return Arrays.stream(parameterizedType.getActualTypeArguments())
                .map(argType -> {
                    if (argType instanceof java.lang.reflect.TypeVariable<?> typeVar) {
                        return resolvedGenericTypeMap().getOrDefault(typeVar, typeVar);
                    }
                    return argType;
                })
                .toArray(Type[]::new);
        } else if (genericType instanceof java.lang.reflect.TypeVariable<?> typeVariable) {
            // If the field itself is a TypeVariable (e.g. T content in Box<T>),
            // try to resolve it from the map.
            Type resolvedType = resolvedGenericTypeMap().get(typeVariable);
            if (resolvedType != null) {
                // If the resolved type is a ParameterizedType (e.g. T becomes List<String>)
                // then its parameters are the actual type arguments.
                if (resolvedType instanceof java.lang.reflect.ParameterizedType parameterizedResolvedType) {
                    return parameterizedResolvedType.getActualTypeArguments();
                }
                // If resolved to a simple Class, it has no further parameters in this context.
                return new Type[]{resolvedType};
            }
        }
        // Fallback for non-parameterized types or unresolvable type variables
        return new Type[]{field.getType()}; // return raw type as an array
    }

    @Override
    public Map<java.lang.reflect.TypeVariable<?>, Type> getResolvedGenericTypeMap() {
        return resolvedGenericTypeMap != null ? resolvedGenericTypeMap : java.util.Collections.emptyMap();
    }
}
