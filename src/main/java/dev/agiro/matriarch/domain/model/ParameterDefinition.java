package dev.agiro.matriarch.domain.model;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Arrays;


public record ParameterDefinition(Parameter parameter,
                                  Map<String, Overrider> overrideValues,
                                  String overrideCoordinate,
                                  Map<java.lang.reflect.TypeVariable<?>, Type> resolvedGenericTypeMap) implements Definition {

    public ParameterDefinition(Parameter parameter, Map<String, Overrider> overrideValues, String overrideCoordinate) {
        this(parameter, overrideValues, overrideCoordinate, java.util.Collections.emptyMap());
    }

    @Override
    public Class<?> clazz() {
        Type paramType = parameter.getParameterizedType();
        if (paramType instanceof java.lang.reflect.TypeVariable<?> typeVariable) {
            Type resolvedType = resolvedGenericTypeMap().get(typeVariable);
            if (resolvedType instanceof Class<?> resolvedClass) {
                return resolvedClass;
            } else if (resolvedType instanceof java.lang.reflect.ParameterizedType parameterizedResolvedType) {
                return (Class<?>) parameterizedResolvedType.getRawType();
            }
            // Fallback if type variable is not in map or not a Class
        }
        return parameter.getType(); // Returns the raw type if not a resolvable TypeVariable
    }

    @Override
    public Type[] parametrizedType() {
        Type genericType = parameter.getParameterizedType();

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
            Type resolvedType = resolvedGenericTypeMap().get(typeVariable);
            if (resolvedType != null) {
                if (resolvedType instanceof java.lang.reflect.ParameterizedType parameterizedResolvedType) {
                    return parameterizedResolvedType.getActualTypeArguments();
                }
                return new Type[]{resolvedType};
            }
        }
        // Fallback for non-parameterized types or unresolvable type variables
        return new Type[]{parameter.getType()};
    }

    @Override
    public Map<java.lang.reflect.TypeVariable<?>, Type> getResolvedGenericTypeMap() {
        return resolvedGenericTypeMap != null ? resolvedGenericTypeMap : java.util.Collections.emptyMap();
    }
}
