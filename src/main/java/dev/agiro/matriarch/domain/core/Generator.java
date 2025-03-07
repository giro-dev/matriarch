package dev.agiro.matriarch.domain.core;


import dev.agiro.matriarch.domain.core.generators.AbstractGenerator;
import dev.agiro.matriarch.domain.core.generators.ClazzGenerators;
import dev.agiro.matriarch.domain.core.generators.MultiGenerator;
import dev.agiro.matriarch.domain.model.Definition;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public class Generator implements Function<Definition, Object> {
    
    private Map<ClazzGenerators, AbstractGenerator<?>> generators = new EnumMap<>(ClazzGenerators.class);
    
    public Generator() {

        Arrays.stream(ClazzGenerators.values())
                .forEach(clazz -> {
                    try {
                        final Class<? extends AbstractGenerator<?>> generator = clazz.getGenerator();
                        final AbstractGenerator<?> abstractGenerator = MultiGenerator.class.isAssignableFrom(generator) ?
                                generator.getDeclaredConstructor(Map.class).newInstance(generators) :
                                generator.getDeclaredConstructor().newInstance();
                        generators.put(clazz, abstractGenerator);
                    } catch (Exception e) {
                        throw new IllegalCallerException("Could not instantiate generator %s: %s".formatted(clazz.getGenerator().getCanonicalName(), e.getMessage()));
                    }
                });
    }

    @Override
    public Object apply(Definition supplierInput) {
        final AbstractGenerator<?> abstractGenerator = generators.get(ClazzGenerators.forClass(supplierInput.clazz()));
        return abstractGenerator.apply(supplierInput);

    }
}
