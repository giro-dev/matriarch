package dev.agiro.matriarch.domain.core.field_solver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import dev.agiro.matriarch.domain.model.*;
import dev.agiro.matriarch.infrastructure.OverridesKnownPatternsStore;
import net.datafaker.Faker;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static dev.agiro.matriarch.domain.model.FieldSupplierType.*;

public class FieldValueSupplier extends EnumMap<FieldSupplierType, Function<FieldValueSupplier.SupplierInput, Object>> {

    private final Random random = new Random();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new ParameterNamesModule())
            .registerModule(new JavaTimeModule());

    private final Function<FieldProperties, Object> randomValue;
    private final Function<ClassProperties<?>, Object> randomObject;
    private final List<Supplier<String>>            stringGenerators;
    private final Map<String, Supplier<String>>     knownPatterns;

    public record SupplierInput(FieldProperties fieldProps) {
    }

    private final Faker faker = new Faker();

    public FieldValueSupplier(Function<FieldProperties, Object> randomValue,
                              Function<ClassProperties<?>, Object> randomObject) {
        super(FieldSupplierType.class);
        this.randomValue      = randomValue;
        this.randomObject     = randomObject;
        this.knownPatterns    = addKnownPatterns(OverridesKnownPatternsStore.getInstance());
        this.stringGenerators = List.of(() -> faker.lorem().word(),
                                        () -> faker.harryPotter().character(),
                                        () -> faker.animal().scientificName(),
                                        () -> faker.bigBangTheory().character(),
                                        () -> faker.lordOfTheRings().character(),
                                        () -> faker.lordOfTheRings().location(),
                                        () -> faker.address().cityName(),
                                        () -> faker.artist().name());
        put(STRING, this::makeString);
        put(INTEGER, input -> random.nextInt());
        put(LONG, input -> random.nextLong());
        put(DOUBLE, input -> random.nextDouble());
        put(BOOLEAN, input -> random.nextBoolean());
        put(FLOAT, input -> random.nextFloat());
        put(CHARACTER, input -> (char) ('a' + new Random().nextInt(26)));
        put(INSTANT, input -> Instant.now());
        put(TIMESTAMP, input -> new java.sql.Timestamp(System.currentTimeMillis() - (long) (Math.random() * 1_000_000_000)));        put(UUID, input -> java.util.UUID.randomUUID());
        put(BIG_DECIMAL, input -> BigDecimal.valueOf(random.nextFloat()));
        put(DATETIME_FORMATER, input -> DateTimeFormatter.BASIC_ISO_DATE);
        put(DATE, input -> faker.date().birthday().toLocalDateTime().toLocalDate());
        put(ARRAY, this::makeArray);
        put(LIST, this::makeList);
        put(SET, this::makeSet);
        put(MAP, this::makeMap);
        put(ENUM, input -> input.fieldProps().fieldType()
                    .getEnumConstants()[random.nextInt(input.fieldProps().fieldType().getEnumConstants().length)]);
    }

    public Object supplyValue(FieldProperties fieldProps) {
        var overrideValue = fieldProps.overrideValues().get(fieldProps.overrideCoordinate());
        return overrideValue == null ?
                get(FieldSupplierType.forClass(fieldProps.fieldType()))
                        .apply(new FieldValueSupplier.SupplierInput(fieldProps)) :
                objectMapper.convertValue(getOverrideValue(fieldProps.overrideValues().get(fieldProps.overrideCoordinate())),
                                          fieldProps.fieldType());
    }

    private String getOverrideValue(Overrider overrideValue) {
        return overrideValue.isRegexPattern() ?
                faker.regexify(overrideValue.value()) :
                overrideValue.value();
    }

    private static Set<Integer> getCollectionElements(Map<String, Overrider> overrideValues,
                                                      String overrideCoordinate) {
        final String regex = overrideCoordinate + "\\.\\[\\d*\\].*";
        return overrideValues.keySet().stream()
                .filter(coordinate -> coordinate.matches(regex))
                .map(c -> c.substring(c.lastIndexOf('[') + 1, c.lastIndexOf(']')))
                .map(Integer::valueOf)
                .collect(Collectors.toSet());
    }

    private Object makeString(SupplierInput input) {
        final Supplier<String> stringSupplier = knownPatterns.entrySet().stream()
                .filter(entry -> input.fieldProps().overrideCoordinate().toLowerCase().contains(entry.getKey().toLowerCase()))
                .map(Entry::getValue)
                .findFirst().orElse(stringGenerators.get(random.nextInt(stringGenerators.size())));
        return stringSupplier.get();
    }

    private Map<String, Supplier<String>> addKnownPatterns(OverridesKnownPatternsStore knownPatternsStore) {
        final Map<String, Supplier<String>> patterns = new HashMap<>();
        knownPatternsStore.getKnownPatterns().patterns().forEach(pattern -> {
            switch(PatternType.valueOf(pattern.type().toUpperCase())){
                case STRING -> patterns.put(pattern.coordinate(), pattern::value);
                case REGEX -> patterns.put(pattern.coordinate(), () -> faker.regexify(pattern.value()));
                case LIST -> {
                    final List<String> list = Arrays.stream(pattern.value().split(","))
                            .map(String::trim)
                            .toList();
                    patterns.put(pattern.coordinate(), () -> list.get(random.nextInt(list.size())));
                }

            }});
        return patterns;
    }

    private Object makeList(SupplierInput input) {
        final var aClass = extractClassFromParametrizedType(input.fieldProps().parametrizedType()).get(0);

        final Set<Integer> collectionElements = getCollectionElements(input.fieldProps().overrideValues(),
                                                                      input.fieldProps().overrideCoordinate());
        final Optional<Integer> size = collectionElements.stream().max(Integer::compareTo);
        return new ArrayList<>(size.map(integer -> IntStream.range(0, integer + 1)
                        .mapToObj(i -> this.randomObject.apply(new ClassProperties<>(aClass,
                                                                                   input.fieldProps().overrideValues(),
                                                                                   (input.fieldProps().overrideCoordinate() + ".[" + i + "]"))))
                        .collect(Collectors.toList()))
                                       .orElseGet(() -> aClass == null ?
                                               Collections.emptyList() :
                                               List.of(this.randomValue.apply(new FieldProperties(aClass,
                                                                                                  input.fieldProps().parametrizedType(),
                                                                                                  input.fieldProps().overrideValues(),
                                                                                                  input.fieldProps().overrideCoordinate())))));
    }

    private Object makeArray(SupplierInput input) {
        Class<?> componentType = input.fieldProps().fieldType().getComponentType();
        return List.of(this.randomValue.apply(new FieldProperties(componentType,
                                                                  input.fieldProps().parametrizedType(),
                                                                  input.fieldProps().overrideValues(),
                                                                  input.fieldProps().overrideCoordinate())))
                .toArray();
    }



    private Object makeSet(SupplierInput input) {
        final var aClass = extractClassFromParametrizedType(input.fieldProps().parametrizedType()).get(0);

        final Set<Integer>      collectionElements = getCollectionElements(input.fieldProps().overrideValues(),
                                                                           input.fieldProps().overrideCoordinate());
        final Optional<Integer> size               = collectionElements.stream().max(Integer::compareTo);
        return size.map(integer -> IntStream.range(0, integer + 1)
                        .mapToObj(i -> this.randomObject.apply(new ClassProperties<>(aClass,
                                                                                   input.fieldProps().overrideValues(),
                                                                                   (input.fieldProps().overrideCoordinate() + ".[" + i + "]"))))
                        .collect(Collectors.toSet()))
                .orElseGet(() -> aClass == null ?
                        Collections.emptySet() :
                        Set.of(this.randomValue.apply(new FieldProperties(aClass,
                                                                          input.fieldProps().parametrizedType(),
                                                                          input.fieldProps().overrideValues(),
                                                                          input.fieldProps().overrideCoordinate()))));
    }

    private Object makeMap(SupplierInput input) {
        final var aClass = extractClassFromParametrizedType(input.fieldProps().parametrizedType());

        final Set<Integer>      collectionElements = getCollectionElements(input.fieldProps().overrideValues(),
                                                                           input.fieldProps().overrideCoordinate());
        final Optional<Integer> size               = collectionElements.stream().max(Integer::compareTo);
        return size.map(integer -> IntStream.range(0, integer + 1)
                        .mapToObj(i -> Pair.of(this.randomObject.apply(new ClassProperties<>(aClass.get(0),
                                                                                               input.fieldProps().overrideValues(),
                                                                                               (input.fieldProps().overrideCoordinate() + ".[" + i + "]"))),
                                                   this.randomObject.apply(new ClassProperties<>(aClass.get(1),
                                                                                                 input.fieldProps().overrideValues(),
                                                                                                 (input.fieldProps().overrideCoordinate() + ".[" + i + "]")))))
                        .collect(Collectors.toMap(Pair::getLeft, Pair::getRight)))
                .orElseGet(() -> aClass == null ?
                        Collections.emptyMap() :
                        Map.of(this.randomValue.apply(new FieldProperties(aClass.get(0),
                                                                          input.fieldProps().parametrizedType(),
                                                                          input.fieldProps().overrideValues(),
                                                                          input.fieldProps().overrideCoordinate())),
                               this.randomValue.apply(new FieldProperties(aClass.get(1),
                                                                          input.fieldProps().parametrizedType(),
                                                                          input.fieldProps().overrideValues(),
                                                                          input.fieldProps().overrideCoordinate()))));
    }

    private static List<Class<?>> extractClassFromParametrizedType(Type parametrizedType) {
        if (parametrizedType instanceof final ParameterizedType type) {

                return Arrays.stream(type.getActualTypeArguments())
                        .map(typeArg -> {
                            try {
                                return Class.forName(typeArg.getTypeName());
                            } catch (ClassNotFoundException e) {
                                return ObjectUtils.Null.class;
                            }
                        })
                        .filter(clazz -> !ObjectUtils.Null.class.equals(clazz))
                        .toList();
        }
        return null;
    }


}
