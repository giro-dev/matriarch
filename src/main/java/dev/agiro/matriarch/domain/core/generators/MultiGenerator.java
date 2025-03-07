package dev.agiro.matriarch.domain.core.generators;


import java.util.Map;

public interface MultiGenerator {

    default AbstractGenerator<?> generatorFor(Class<?> aClass) {
        return getGenerators().get(ClazzGenerators.forClass(aClass));
    }

    Map<ClazzGenerators, AbstractGenerator<?>> getGenerators();


}
