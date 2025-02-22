package dev.agiro.matriarch.domain.core.generators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import dev.agiro.matriarch.domain.model.FieldProperties;
import dev.agiro.matriarch.domain.model.Overrider;
import dev.agiro.matriarch.domain.model.PatternType;
import dev.agiro.matriarch.infrastructure.KnownPatternsStore;
import net.datafaker.Faker;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractGenerator<T> implements Function<FieldProperties, T> {

    private static final ObjectMapper       objectMapper  = new ObjectMapper()
            .registerModule(new ParameterNamesModule())
            //.registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());

    static Map<String, Supplier<String>> patterns = new HashMap<>();

    static {
        KnownPatternsStore.getInstance().getPatterns().patterns()
                .forEach(pattern -> {
                    switch (PatternType.valueOf(pattern.type().toUpperCase())) {
                        case STRING -> patterns.put(pattern.coordinate(), pattern::value);
                        case REGEX -> patterns.put(pattern.coordinate(), () -> new Faker().regexify(pattern.value()));
                        case LIST -> {
                            final List<String> list = Arrays.stream(pattern.value().split(","))
                                    .map(String::trim)
                                    .toList();
                            patterns.put(pattern.coordinate(), () -> list.get(new SecureRandom().nextInt(list.size())));
                        }

                    }
                });
    }

    Function<FieldProperties, Optional<T>> override = input -> {

        if (input.overrideValues().containsKey(input.overrideCoordinate())) {
            final var overrider = input.overrideValues().get(input.overrideCoordinate());
            if (Overrider.OverriderType.OBJECT.equals(overrider.type()) && getClazz().equals(input.fieldType())) {
                return Optional.of(getClazz().cast(overrider.value()));
            } else {
                return Optional.of(getClazz().cast(objectMapper.convertValue(overrider.value(), input.fieldType())));
            }
        }
        return patterns.entrySet().stream()
                .filter(entry -> input.overrideCoordinate().toLowerCase().contains(entry.getKey().toLowerCase()))
                .map(pattern -> getClazz().cast(pattern.getValue().get()))
                .findFirst();
    };

    abstract Class<T> getClazz();



    @Override
    public T apply(FieldProperties supplierInput){
        return override.apply(supplierInput).orElse(generate(supplierInput));
    }

    abstract T generate(FieldProperties supplierInput);


}
