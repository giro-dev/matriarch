package dev.agiro.matriarch.domain.model;

import java.lang.reflect.Type;
import java.util.Map;

public record FieldProperties(Class<?> fieldType,
                              Type parametrizedType,
                              Map<String, Overrider> overrideValues,
                              String overrideCoordinate) {
}
