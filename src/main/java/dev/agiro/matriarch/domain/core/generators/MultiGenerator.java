package dev.agiro.matriarch.domain.core.generators;


import java.util.Map;

public interface MultiGenerator {

    default AbstractGenerator<?> generatorFor(Class<?> aClass) {
        return getGenerator().get(ClazzGenerators.forClass(aClass));
    }

    Map<ClazzGenerators, AbstractGenerator<?>> getGenerator();


}
