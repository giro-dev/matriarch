package dev.agiro.matriarch.domain.core;


import dev.agiro.matriarch.domain.core.field_solver.FieldValueSupplier;
import dev.agiro.matriarch.domain.model.ClassProperties;
import dev.agiro.matriarch.domain.model.FieldProperties;
import dev.agiro.matriarch.domain.model.FieldSupplierType;
import dev.agiro.matriarch.domain.model.Overrider;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static dev.agiro.matriarch.domain.model.FieldSupplierType.UNKNOWN;


public class ObjectMotherGenerator {
    Logger log = Logger.getLogger(ObjectMotherGenerator.class.getName());

    enum InstanceType {NO_ARGS_CONSTRUCTOR, STATIC_METHOD, CONSTRUCTOR}

    private final FieldValueSupplier fieldValueSupplier = new FieldValueSupplier(this::generateRandomValue,
                                                                                 this::createObject);

    public <T> T createObject(Class<T> clazz) {
        return createObject(new ClassProperties<>(clazz, Map.of(), ""));
    }

    public <T> T createObject(Class<T> clazz, Map<String, Overrider> overrideValues) {
        return createObject(new ClassProperties<>(clazz, overrideValues, ""));
    }

    public <T> T createObject(ClassProperties<T> classPropperties) {
        final var instance = getInstance(classPropperties.fieldType(),
                                         classPropperties.overrideValues(),
                                         classPropperties.overrideCoordinate());
        resolveFields(instance.getLeft().getClass())
                .forEach(field -> setValueToField(instance.getLeft(),
                                                  field,
                                                  classPropperties.overrideValues(),
                                                  classPropperties.overrideCoordinate()));

        return instance.getLeft();

    }

    private List<Field> resolveFields(Class<?> aClass) {
        final List<Field> declaredFields = new ArrayList<>();
        declaredFields.addAll(List.of(Arrays.stream(aClass.getDeclaredFields())
                                              .filter(field -> !Modifier.isStatic(field.getModifiers()))
                                              .toArray(Field[]::new)));
        declaredFields.addAll(List.of(Arrays.stream(aClass.getSuperclass().getDeclaredFields())
                                              .filter(field -> !Modifier.isStatic(field.getModifiers()))
                                              .toArray(Field[]::new)));
        return declaredFields;
    }

    private void setValueToField(Object object,
                                 Field field,
                                 Map<String, Overrider> overrideValues,
                                 String currentField) {

        try {
            boolean isAccessible = field.isAccessible();
            if (!Modifier.isFinal(field.getModifiers())) {

                field.setAccessible(true);
                String fieldName = currentField.isEmpty() ? field.getName() : currentField + "." + field.getName();
                Object value;

                value = generateRandomValue(new FieldProperties(field.getType(),
                                                                field.getGenericType(),
                                                                overrideValues,
                                                                fieldName));

                field.set(object, value);
                field.setAccessible(isAccessible);
            }
        } catch (Exception e) {
            log.severe("ObjectMother not able to set field %s in class %s with value %s".formatted(field.getName(),
                                                                                                   object.getClass().getName(),
                                                                                                   e.getMessage()));
        }
    }

    private <T> Pair<T, InstanceType> getInstance(Class<T> clazz,
                                                  Map<String, Overrider> overrideValues,
                                                  String overrideCoordinate) {
        final Map<List<Parameter>, ? extends Constructor<?>> constructors = Arrays.stream(clazz.getConstructors())
                .collect(Collectors.toMap(
                        c -> List.of(c.getParameters()),
                        c -> c));
        Constructor<?> constructor = null;
        try {
            if (constructors.isEmpty())
                return Pair.of(tryStaticMethod(clazz, overrideValues, overrideCoordinate), InstanceType.STATIC_METHOD);
            constructor = constructors.entrySet().stream()
                    .reduce((entry1, entry2) -> entry1.getKey().isEmpty()
                                                || entry1.getKey().size() > entry2.getKey().size()
                            ? entry1 : entry2)
                    .map(Map.Entry::getValue)
                    .orElseThrow(() -> new RuntimeException("No constructor found in class " + clazz.getName()));

            var type = constructor.getParameterCount() == 0 ? InstanceType.NO_ARGS_CONSTRUCTOR : InstanceType.CONSTRUCTOR;
            final Object[] parameters = Arrays.stream(constructor.getParameters())
                    .map(parameter -> generateRandomValue(new FieldProperties(parameter.getType(),
                                                                              parameter.getParameterizedType(),
                                                                              overrideValues,
                                                                              overrideCoordinate.isEmpty() ? parameter.getName() :
                                                                                      overrideCoordinate + "." + parameter.getName()))).toArray();
            return Pair.of((T) constructor.newInstance(parameters), type);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error instantiating class %s with constructor %s: %s",
                                                     clazz,
                                                     constructor == null ? "not constructor found" : Arrays.stream(
                                                                     constructor.getParameters())
                                                             .map(parameter -> parameter.getType().getSimpleName() + " " + parameter.getName())
                                                             .collect(Collectors.joining(",  ", "(", ")")),
                                                     e.getMessage()));
        }
    }

    private <T> T tryStaticMethod(Class<T> clazz,
                                  Map<String, Overrider> overrideValues,
                                  String overrideCoordinate) throws InvocationTargetException, IllegalAccessException {
        final Method creationMethod = Arrays.stream(clazz.getMethods())
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .filter(method -> method.getReturnType().equals(clazz))
                .findFirst().orElseThrow(() -> new RuntimeException("No static method found in class " + clazz.getName()));
        final Object[] parameters = Arrays.stream(creationMethod.getParameters())
                .map(parameter -> {
                    try {
                        return generateRandomValue(new FieldProperties(parameter.getType(),
                                                                       parameter.getParameterizedType(),
                                                                       overrideValues,
                                                                       overrideCoordinate.isEmpty() ? parameter.getName() :
                                                                               overrideCoordinate + "." + parameter.getName()));

                    } catch (Exception e) {
                        log.severe("ObjectMother not able to generate parameter %s for method %s in class %s"
                                           .formatted(
                                                   parameter.getType().getSimpleName(),
                                                   creationMethod.getName(),
                                                   clazz.getName()));
                        return null;
                    }
                }).toArray();
        return (T) creationMethod.invoke(null, parameters);
    }

    private Object generateRandomValue(FieldProperties fieldProperties) {
        final FieldSupplierType supplierType = FieldSupplierType.forClass(fieldProperties.fieldType());

        if (UNKNOWN.equals(supplierType)) {
            //try creating the field as an object (Recursive method)
            return createObject(new ClassProperties<>(fieldProperties.fieldType(),
                                                    fieldProperties.overrideValues(),
                                                    fieldProperties.overrideCoordinate()));
        }

        return fieldValueSupplier.supplyValue(fieldProperties);
    }


}



