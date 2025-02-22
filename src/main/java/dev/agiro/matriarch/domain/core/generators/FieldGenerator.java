package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.FieldProperties;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FieldGenerator implements Function<FieldProperties, Object> {
    
    private Map<Class<?>, AbstractGenerator<?>> generators = new HashMap<>();
    
    public FieldGenerator() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                                                          .setUrls(ClasspathHelper.forPackage(this.getClass().getPackageName()))
                                                          .addScanners(Scanners.SubTypes));
        reflections.getSubTypesOf(AbstractGenerator.class)
                .forEach(generator -> {
                    try {
                        final AbstractGenerator<?> abstractGenerator = generator.getDeclaredConstructor().newInstance();
                        generators.put(abstractGenerator.getClazz(), abstractGenerator);
                    } catch (Exception e) {
                        throw new IllegalCallerException("Could not instantiate generator %s: %s".formatted(generator.getCanonicalName(), e.getMessage()));
                    }
                });
    }

    @Override
    public Object apply(FieldProperties supplierInput) {
        final var clazz = supplierInput.fieldType().isPrimitive() ? getWrapperClass(supplierInput.fieldType()) : supplierInput.fieldType();
        return generators.get(clazz).apply(supplierInput);
                
    }
    private static Class<?> getWrapperClass(Class<?> clazz) {
        if (clazz == int.class) return Integer.class;
        if (clazz == long.class) return Long.class;
        if (clazz == double.class) return Double.class;
        if (clazz == float.class) return Float.class;
        if (clazz == boolean.class) return Boolean.class;
        if (clazz == char.class) return Character.class;
        if (clazz == byte.class) return Byte.class;
        if (clazz == short.class) return Short.class;
        if (clazz == void.class) return Void.class;
        throw new IllegalArgumentException("Unknown primitive type: " + clazz);
    }
}
