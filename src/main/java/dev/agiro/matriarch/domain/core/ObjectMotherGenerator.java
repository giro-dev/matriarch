package dev.agiro.matriarch.domain.core;


import dev.agiro.matriarch.domain.model.ClassDefinition;
import dev.agiro.matriarch.domain.model.Overrider;

import java.util.Map;



public class ObjectMotherGenerator {


    private final Generator fieldValueSupplier = new Generator();

    public <T> T createObject(Class<T> clazz) {
        return createObject(new ClassDefinition<>(clazz, Map.of(), ""));
    }

    public <T> T createObject(Class<T> clazz, Map<String, Overrider> overrideValues) {
        return createObject(new ClassDefinition<>(clazz, overrideValues, ""));
    }

    public  <T> T createObject(ClassDefinition<T> classDefinition) {
        return classDefinition.aClass().cast(fieldValueSupplier.apply(classDefinition));

    }


}



