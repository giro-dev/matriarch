package dev.agiro.matriarch.domain.model;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.Map;

public interface Definition {
    Class<?> clazz();

    Type[] parametrizedType();

    Map<String, Overrider> overrideValues();

    String overrideCoordinate();

    default Map<TypeVariable<?>, Type> getResolvedGenericTypeMap() {
        return Collections.emptyMap();
    }
}
