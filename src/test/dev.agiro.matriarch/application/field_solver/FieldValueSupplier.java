package dev.agiro.matriarch.application.field_solver;


import dev.agiro.matriarch.model.ClassProperties;
import dev.agiro.matriarch.model.FieldProperties;
import dev.agiro.matriarch.model.Overrider;
import net.datafaker.Faker;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class FieldValueSupplier extends HashMap<Class<?>, Function<FieldValueSupplier.SupplierInput, Object>> {

    private final Function<FieldProperties, Object> randomValue;
    private final Function<ClassProperties, Object> randomObject;
    private final List<Supplier<String>> stringGenerators;

    public record SupplierInput(FieldProperties fieldProps) {
    }

    private final Faker faker = new Faker();

    public FieldValueSupplier(Function<FieldProperties, Object> randomValue,
                              Function<ClassProperties, Object> randomObject) {
        this.randomValue      = randomValue;
        this.randomObject     = randomObject;
        this.stringGenerators = List.of(() -> faker.lorem().word(),
                                        () -> faker.harryPotter().character(),
                                        () -> faker.harryPotter().location(),
                                        () -> faker.harryPotter().book(),
                                        () -> faker.animal().scientificName(),
                                        () -> faker.bigBangTheory().character(),
                                        () -> faker.lordOfTheRings().character(),
                                        () -> faker.lordOfTheRings().location(),
                                        () -> faker.address().cityName(),
                                        () -> faker.beer().name(),
                                        () -> faker.beer().brand(),
                                        () -> faker.computer().windows(),
                                        () -> faker.computer().linux(),
                                        () -> faker.artist().name(),
                                        () -> faker.videoGame().title());
        put(String.class, this::makeString);
        put(Integer.class, input ->  faker.random().nextInt());
        put(int.class, input -> faker.random().nextInt());
        put(long.class, input -> faker.random().nextLong());
        put(Long.class, input -> faker.random().nextLong());
        put(Double.class, input -> faker.random().nextDouble());
        put(double.class, input -> faker.random().nextDouble());
        put(Boolean.class, input -> faker.random().nextBoolean());
        put(boolean.class, input -> faker.random().nextBoolean());
        put(Float.class, input -> faker.random().nextFloat());
        put(float.class, input -> faker.random().nextFloat());
        put(char.class, input -> faker.random().nextInt(26) + 'a');
        put(Character.class, input -> faker.random().nextInt(26) + 'a');
        put(Instant.class, input -> Instant.now());
        put(Timestamp.class, input -> faker.time().past(3, ChronoUnit.DAYS));
        put(UUID.class, input -> UUID.randomUUID());
        put(BigDecimal.class, input -> BigDecimal.valueOf(faker.random().nextFloat()));
        put(DateTimeFormatter.class, input -> DateTimeFormatter.BASIC_ISO_DATE);
        put(Array.class, this::makeArray);
        put(List.class, this::makeList);
        put(Set.class, this::makeSet);
    }

    private Object makeString(SupplierInput input) {
        return stringGenerators.get(faker.random().nextInt(stringGenerators.size())).get();
    }

    private Object makeList(SupplierInput input) {
        final Class<?> aClass = extractClassFromParametrizedType(input.fieldProps().parametrizedType());

        final Map<String, String> collectionElements = getCollectionElements(input.fieldProps().overrideValues(), input.fieldProps().overrideCoordinate());
        final Optional<Integer>   size               = collectionElements.keySet().stream().map(Integer::valueOf).max(Integer::compareTo);
        return size.map(integer -> IntStream.range(0, integer + 1)
                .mapToObj(i -> this.randomObject.apply(new ClassProperties(aClass,
                                                                           input.fieldProps().overrideValues(),
                                                                           (input.fieldProps().overrideCoordinate() + ".[" + i + "]"))))
                .collect(Collectors.toList()))
                .orElseGet(() -> aClass == null ?
                        Collections.emptyList() :
                        List.of(this.randomValue.apply(new FieldProperties(aClass, input.fieldProps().parametrizedType(),input.fieldProps().overrideValues(), input.fieldProps().overrideCoordinate() ))));
    }
    private Object makeSet(SupplierInput input) {
        final Class<?> aClass = extractClassFromParametrizedType(input.fieldProps().parametrizedType());

        final Map<String, String> collectionElements = getCollectionElements(input.fieldProps().overrideValues(), input.fieldProps().overrideCoordinate());
        final Optional<Integer>   size               = collectionElements.keySet().stream().map(Integer::valueOf).max(Integer::compareTo);
        return size.map(integer -> IntStream.range(0, integer + 1)
                .mapToObj(i -> this.randomObject.apply(new ClassProperties(aClass,
                                                                           input.fieldProps().overrideValues(),
                                                                           (input.fieldProps().overrideCoordinate() + ".[" + i + "]"))))
                .collect(Collectors.toSet()))
                .orElseGet(() -> aClass == null ?
                        Collections.emptySet() :
                        Set.of(this.randomValue.apply(new FieldProperties(aClass, input.fieldProps().parametrizedType(),input.fieldProps().overrideValues(), input.fieldProps().overrideCoordinate() ))));
    }

    private Object makeArray(SupplierInput input) {
        Class<?> componentType = input.fieldProps().fieldType().getComponentType();
        return List.of(this.randomValue.apply(new FieldProperties(componentType, input.fieldProps().parametrizedType(),input.fieldProps().overrideValues(), input.fieldProps().overrideCoordinate() )))
                .toArray();
    }
/*

        if (fieldType == Map.class) {

            //TODO calculate and define
            final Class<?> aClass = extractClassFromParametrizedType(parametrizedType);

            final Map<String, String> collectionElements = getCollectionElements(overrideValues, overrideCoordinate);
            final Optional<Integer> size = collectionElements.keySet().stream().map(Integer::valueOf).max(Integer::compareTo);
            if(size.isPresent()){
                return IntStream.range(0,size.get() + 1)
                        .mapToObj(i -> motherCreate(aClass, overrideValues, (overrideCoordinate + ".[" + i + "]")))
                        .collect(Collectors.toSet());
            }
            return Collections.emptyMap();
        }
    }
*/
    public Function<SupplierInput, Object> supplier(Class<?> type) {
        if (type.isEnum()) {
            return fieldProps -> type.getEnumConstants()[faker.random().nextInt(type.getEnumConstants().length)];
        }
        return get(type);
    }

    private static Map<String, String> getCollectionElements(Map<String, Overrider> overrideValues,
                                                             String overrideCoordinate) {
        final String regex = overrideCoordinate + "\\.\\[\\d*\\].*";
        return overrideValues.keySet().stream()
                .filter(coordinate -> coordinate.matches(regex))
                .collect(Collectors.toMap(c -> c.substring(c.lastIndexOf('[') +1, c.lastIndexOf(']')),
                                          c -> c));
    }

    private static Class<?> extractClassFromParametrizedType(Type parametrizedType){
        if (parametrizedType instanceof ParameterizedType) {
            ParameterizedType type        = (ParameterizedType) parametrizedType;
            Type              elementType = type.getActualTypeArguments()[0];
            try {
                return Class.forName(elementType.getTypeName());
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
        return null;
    }


}
