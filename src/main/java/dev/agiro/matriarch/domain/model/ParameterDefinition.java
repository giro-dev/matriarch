package dev.agiro.matriarch.domain.model;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Map;

public record ParameterDefinition(Parameter parameter, Map<String, Overrider> overrideValues, String overrideCoordinate) implements Definition {

    @Override
    public Class<?> clazz() {
        return parameter.getType();
    }

    @Override
    public Type[] parametrizedType() {
        final Type genericType = parameter.getParameterizedType();
        if (genericType instanceof java.lang.reflect.ParameterizedType parameterizedType) {
            return parameterizedType.getActualTypeArguments();
        }
        return new Type[]{genericType};
    }
}
