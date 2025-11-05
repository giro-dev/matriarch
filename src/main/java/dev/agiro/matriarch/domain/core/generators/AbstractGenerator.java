package dev.agiro.matriarch.domain.core.generators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import dev.agiro.matriarch.domain.model.Definition;
import dev.agiro.matriarch.domain.model.PatternType;
import dev.agiro.matriarch.infrastructure.KnownPatternsStore;
import net.datafaker.Faker;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractGenerator<T> implements Function<Definition, T> {

    private static final ObjectMapper       objectMapper  = new ObjectMapper()
            .registerModule(new ParameterNamesModule())
            //.registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());

    static Map<String, Supplier<String>> patterns = new HashMap<>();

    static {
        KnownPatternsStore.getInstance().getPatterns().getPatterns()
                .forEach(pattern -> {
                    switch (PatternType.valueOf(pattern.getType().toUpperCase())) {
                        case STRING -> patterns.put(pattern.getCoordinate(), pattern::getValue);
                        case REGEX -> patterns.put(pattern.getCoordinate(), () -> new Faker().regexify(pattern.getValue()));
                        case LIST -> {
                            final List<String> list = Arrays.stream(pattern.getValue().split(","))
                                    .map(String::trim)
                                    .toList();
                            patterns.put(pattern.getCoordinate(), () -> list.get(new SecureRandom().nextInt(list.size())));
                        }

                    }
                });
    }

    Function<Definition, Optional<T>> override = input -> {

        if (input.overrideValues().containsKey(input.overrideCoordinate())) {
            final var overrider = input.overrideValues().get(input.overrideCoordinate());
            switch (overrider.type()) {
                case NULL -> {
                    return Optional.empty();
                }
                case SUPPLIER -> {
                    @SuppressWarnings("unchecked")
                    Supplier<Object> supplier = (Supplier<Object>) overrider.value();
                    Object suppliedValue = supplier.get();
                    if (suppliedValue == null) {
                        return Optional.empty();
                    }
                    if (suppliedValue.getClass().equals(input.clazz()) || getClazz().isInstance(suppliedValue)) {
                        return Optional.of(getClazz().cast(suppliedValue));
                    }
                    return Optional.of(getClazz().cast(objectMapper.convertValue(suppliedValue, input.clazz())));
                }
                case OBJECT, STRING -> {
                    if (overrider.value().getClass().equals(input.clazz())) {
                        return Optional.of(getClazz().cast(overrider.value()));
                    }
                    return Optional.of(getClazz().cast(objectMapper.convertValue(overrider.value(),
                                                                                 input.clazz())));
                }
                case REGEX -> {
                    return Optional.of(getClazz().cast(objectMapper.convertValue(new Faker().regexify((String)overrider.value()),
                                                                                 input.clazz())));
                }
            }
        }
        return patterns.entrySet().stream()
                .filter(entry -> input.overrideCoordinate().toLowerCase().contains(entry.getKey().toLowerCase()))
                .map(pattern -> getClazz().cast(objectMapper.convertValue(pattern.getValue().get(), input.clazz())))
                .findFirst();
    };

    private final Class<T> fieldType;

    protected AbstractGenerator(Class<T> fieldType) {
        this.fieldType = fieldType;
    }

    public Class<T> getClazz(){
        return fieldType;
    }



    @Override
    public T apply(Definition supplierInput){
            // Check if there's an explicit NULL override first
            if (supplierInput.overrideValues().containsKey(supplierInput.overrideCoordinate())) {
                final var overrider = supplierInput.overrideValues().get(supplierInput.overrideCoordinate());
                if (overrider.type() == dev.agiro.matriarch.domain.model.Overrider.OverriderType.NULL) {
                    return null;
                }
            }
            return override.apply(supplierInput).orElse(generate(supplierInput));
    }

    public abstract T generate(Definition supplierInput);


}
