package dev.agiro.matriarch.domain.model;

public interface Definition {
    Class<?> clazz();

    java.lang.reflect.Type[] parametrizedType();

    java.util.Map<String, Overrider> overrideValues();

    String overrideCoordinate();

    default java.util.Map<java.lang.reflect.TypeVariable<?>, java.lang.reflect.Type> getResolvedGenericTypeMap() {
        return java.util.Collections.emptyMap();
    }
}
