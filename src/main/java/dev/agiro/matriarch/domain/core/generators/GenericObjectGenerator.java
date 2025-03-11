package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GenericObjectGenerator extends AbstractGenerator<Object> implements MultiGenerator {

    Logger log = Logger.getLogger(GenericObjectGenerator.class.getName());

    private final Map<ClazzGenerators, AbstractGenerator<?>> generators;

    public GenericObjectGenerator(Map<ClazzGenerators, AbstractGenerator<?>> generators) {
        super(Object.class);
        this.generators = generators;
    }

    @Override
    public Object generate(Definition classDefinition) {
        final var overrideValues     = classDefinition.overrideValues();
        final var overrideCoordinate = classDefinition.overrideCoordinate();
        if (overrideValues.containsKey(overrideCoordinate)
            && Overrider.OverriderType.OBJECT.equals(overrideValues.get(overrideCoordinate).type())) {
            return secureCast(classDefinition.clazz(),
                              overrideValues.get(overrideCoordinate).value());
        }

        final var instance = getInstance(classDefinition);
        if (instance.getInstance() == null) {
            return null;
        }
        resolveFields(instance.getInstance().getClass())
                .forEach(field -> setValueToField(instance.getInstance(),
                                                  field,
                                                  overrideValues,
                                                  overrideCoordinate));

        return instance.getInstance();

    }

    private void setValueToField(Object object,
                                 Field field,
                                 Map<String, Overrider> overrideValues,
                                 String currentField) {

        try {
            if (!Modifier.isFinal(field.getModifiers())) {

                String fieldName = currentField.isEmpty() ? field.getName() : currentField + "." + field.getName();

                field.setAccessible(true);
                field.set(object, generateValue(new FieldDefinition(field,
                                                                    overrideValues,
                                                                    fieldName)));
            }
        } catch (Exception e) {
            final var setter = Arrays.stream(object.getClass().getMethods())
                    .filter(method -> method.getName().equals("set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1)))
                    .findFirst();

            if (setter.isPresent()) {
                try {
                    setter.get().invoke(object, generateValue(new FieldDefinition(field,
                                                                                  overrideValues,
                                                                                  currentField.isEmpty() ? field.getName() : currentField + "." + field.getName())));
                } catch (Exception ex) {
                    log.finest(() -> "ObjectMother not able to set field %s in class %s nor reflection or the method %s. %s".formatted(
                            field.getName(),
                            object.getClass().getName(),
                            setter.get().getName(),
                            ex.getMessage()));
                }
            }
            log.severe(() -> "ObjectMother not able to set field %s in class %s with value %s".formatted(
                    field.getName(),
                    object.getClass().getName(),
                    e.getMessage()));
        }
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

    private ConstructorMethod<?> getInstance(Definition classDefinition) {
        final Class<?> clazz = classDefinition.clazz();
        final Map<List<Parameter>, Constructor<?>> constructors = Arrays.stream(clazz.getDeclaredConstructors())
                .collect(Collectors.toMap(
                        c -> List.of(c.getParameters()),
                        c -> c));
        AtomicReference<Constructor<?>> constructor        = new AtomicReference<>();
        final String                    overrideCoordinate = classDefinition.overrideCoordinate();
        try {
            if (constructors.isEmpty()) {
                throw new RuntimeException("No Constructors for class %s".formatted(clazz.getCanonicalName()));
            }
            constructor.set(constructors.entrySet().stream()
                                    .reduce((entry1, entry2) -> entry1.getKey().isEmpty()
                                                                || entry1.getKey().size() > entry2.getKey().size()
                                            ? entry1 : entry2)
                                    .map(Map.Entry::getValue)
                                    .orElseThrow(() -> new RuntimeException("No constructor found in class " + clazz.getName())));

            constructor.get().setAccessible(true);

            var type = constructor.get().getParameterCount() == 0 ? InstanceType.NO_ARGS_CONSTRUCTOR : InstanceType.CONSTRUCTOR;
            final Object[] parameters = Arrays.stream(constructor.get().getParameters())
                    .map(parameter -> generateValue(new ParameterDefinition(parameter,
                                                                            classDefinition.overrideValues(),
                                                                            overrideCoordinate.isEmpty() ? parameter.getName() :
                                                                                    overrideCoordinate + "." + parameter.getName())))
                    .toArray();
            return ConstructorMethod.of(secureCast(clazz, constructor.get().newInstance(parameters)), type);
        } catch (Exception e) {
            log.finest(() -> String.format("Error instantiating class %s with constructor %s: %s",
                                           clazz,
                                           constructor.get() == null ? "not constructor found" : Arrays.stream(
                                                           constructor.get().getParameters())
                                                   .map(parameter -> parameter.getType().getSimpleName() + " " + parameter.getName())
                                                   .collect(Collectors.joining(",  ", "(", ")")),
                                           e.getMessage()));
            log.severe(() -> String.format("Error instantiating class %s with constructor. field %s will be null: %s",
                                           clazz,
                                           overrideCoordinate,
                                           e.getMessage()));
            return ConstructorMethod.of(null, InstanceType.NO_ARGS_CONSTRUCTOR);
        }
    }

    private Object generateValue(Definition definition) {
        final var generator = generators.get(ClazzGenerators.forClass(definition.clazz()));
        return generator.apply(definition);
    }

    private <T> T secureCast(Class<T> tClass, Object value) {
        if (value == null) return null;
        if (tClass.isInstance(value)) {
            return tClass.cast(value);
        } else {
            throw new RuntimeException("Unable to parse object of type %s to %s (%s)".formatted(value.getClass().getCanonicalName(),
                                                                                                tClass.getCanonicalName()
                    , value.toString()));
        }
    }


    @Override
    public Map<ClazzGenerators, AbstractGenerator<?>> getGenerator() {
        return generators;
    }
}
