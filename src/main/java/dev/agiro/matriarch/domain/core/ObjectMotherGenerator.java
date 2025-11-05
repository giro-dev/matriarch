package dev.agiro.matriarch.domain.core;


import dev.agiro.matriarch.domain.model.ClassDefinition;
import dev.agiro.matriarch.domain.model.Overrider;
import dev.agiro.matriarch.domain.model.TypeReference;
import dev.agiro.matriarch.domain.model.TypeReferenceDefinition;

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

    public <T> T createObject(TypeReference<T> typeReference, Map<String, Overrider> overrideValues) {
        return createObject(new TypeReferenceDefinition<>(typeReference, overrideValues, ""));
    }

    @SuppressWarnings("unchecked")
    public <T> T createObject(TypeReferenceDefinition<T> typeReferenceDefinition) {
        return (T) fieldValueSupplier.apply(typeReferenceDefinition);
    }
}
