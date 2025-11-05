package dev.agiro.matriarch.domain.core.generators;



import dev.agiro.matriarch.domain.model.ClassDefinition;
import dev.agiro.matriarch.domain.model.Definition;
import dev.agiro.matriarch.domain.model.Overrider;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

public class MapGenerator extends AbstractGenerator<Map<?,?>> implements MultiGenerator {

    private final Map<ClazzGenerators, AbstractGenerator<?>> generator;

    @SuppressWarnings("unchecked")
    public MapGenerator(Map<ClazzGenerators, AbstractGenerator<?>> generators) {
        super( (Class<Map<?,?>>) (Class<?>) List.class);
        this.generator = generators;
    }


    @Override
    public Map<?,?> generate(Definition supplierInput) {
        try {
            // Check if parametrizedType is available and not null
            if (supplierInput.parametrizedType() == null ||
                supplierInput.parametrizedType().length < 2 ||
                supplierInput.parametrizedType()[0] == null ||
                supplierInput.parametrizedType()[1] == null) {
                // Default to empty map if no type information available
                return java.util.Collections.emptyMap();
            }
            final Class<?> keyClass = Class.forName(supplierInput.parametrizedType()[0].getTypeName());
            var keyGenerator = this.generator.get(ClazzGenerators.forClass(keyClass));
            final Class<?> valueClass = Class.forName(supplierInput.parametrizedType()[1].getTypeName());
            var valueGenerator = this.generator.get(ClazzGenerators.forClass(valueClass));
            final int      listSize      = new SecureRandom().nextInt(1, 15);
            final var regexPattern = Pattern.compile(supplierInput.overrideCoordinate() + "\\[(.*)]");
            List<String> mapOverriders = supplierInput.overrideValues().keySet().stream()
                    .filter(overrider -> regexPattern.matcher(overrider).matches())
                    .toList();
            if (mapOverriders.isEmpty()) {
                return generateRandomValues(supplierInput, listSize, keyGenerator, keyClass, valueGenerator, valueClass);
            }
            return mapOverriders.stream()
                    .map(overrider -> regexPattern.matcher(overrider).replaceAll("$1"))
                    .collect(toMap(string -> keyGenerator.apply(new ClassDefinition<>(keyClass,
                            Map.of(string, Overrider.with(string)),
                            string)),
                            k -> valueGenerator.apply(new ClassDefinition<>(valueClass,
                                    supplierInput.overrideValues(),
                                    supplierInput.overrideCoordinate() + "[%s]".formatted(k)))));


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<?, ?> generateRandomValues(Definition supplierInput, int listSize, AbstractGenerator<?> keyGenerator, Class<?> keyClass, AbstractGenerator<?> valueGenerator, Class<?> valueClass) {
        return IntStream.range(0, listSize)
                .mapToObj(i -> keyGenerator.apply(new ClassDefinition<>(keyClass,
                        supplierInput.overrideValues(),
                        supplierInput.overrideCoordinate() + "[%s]".formatted(i))))
                .collect(toMap(k -> k,
                        k -> valueGenerator.apply(new ClassDefinition<>(valueClass,
                                supplierInput.overrideValues(),
                                supplierInput.overrideCoordinate() + "[%s]".formatted(k)))));
    }


    @Override
    public Map<ClazzGenerators, AbstractGenerator<?>> getGenerator() {
        return generator;
    }

}
