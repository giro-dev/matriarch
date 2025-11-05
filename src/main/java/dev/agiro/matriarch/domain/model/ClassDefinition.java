package dev.agiro.matriarch.domain.model;


import java.lang.reflect.Type;
import java.util.Map;

public record ClassDefinition<T>(Class<T> aClass,
                                 Map<String, Overrider> overrideValues,
                                 String overrideCoordinate) implements Definition {
    @Override
    public Class<?> clazz() {
        return aClass;
    }

    @Override
    public Type[] parametrizedType() {
        final Type genericType = aClass.getGenericSuperclass();
        if (genericType instanceof java.lang.reflect.ParameterizedType parameterizedType) {
            return parameterizedType.getActualTypeArguments();
        }
        return new Type[]{};
    }
}
