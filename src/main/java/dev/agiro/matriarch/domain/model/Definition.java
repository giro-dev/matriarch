package dev.agiro.matriarch.domain.model;

public interface Definition {
    Class<?> clazz();

    java.lang.reflect.Type[] parametrizedType();

    java.util.Map<String, Overrider> overrideValues();

    String overrideCoordinate();
}
