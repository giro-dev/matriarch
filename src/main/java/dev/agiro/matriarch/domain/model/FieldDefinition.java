package dev.agiro.matriarch.domain.model;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

public record FieldDefinition(Field field,
                              Map<String, Overrider> overrideValues,
                              String overrideCoordinate) implements Definition {
    @Override
    public Class<?> clazz() {
        return field.getType();
    }

    @Override
    public Type[] parametrizedType() {
        final Type genericType = field.getGenericType();
        if (genericType instanceof java.lang.reflect.ParameterizedType parameterizedType) {
            return parameterizedType.getActualTypeArguments();
        }
        return new Type[]{genericType};
    }
}
