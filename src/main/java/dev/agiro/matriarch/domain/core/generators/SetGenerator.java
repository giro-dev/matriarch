package dev.agiro.matriarch.domain.core.generators;



import dev.agiro.matriarch.domain.model.ClassDefinition;
import dev.agiro.matriarch.domain.model.Definition;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SetGenerator extends AbstractGenerator<Set<?>> implements MultiGenerator {

    private final Map<ClazzGenerators, AbstractGenerator<?>> generators;

    @SuppressWarnings("unchecked")
    public SetGenerator(Map<ClazzGenerators, AbstractGenerator<?>> generators) {
        super( (Class<Set<?>>) (Class<?>) List.class);
        this.generators = generators;
    }


    @Override
    public Set<?> generate(Definition supplierInput) {
        try {
            // Check if parametrizedType is available and not null
            if (supplierInput.parametrizedType() == null ||
                supplierInput.parametrizedType().length == 0 ||
                supplierInput.parametrizedType()[0] == null) {
                // Default to empty set if no type information available
                return java.util.Collections.emptySet();
            }
            final Class<?> aClass = Class.forName(supplierInput.parametrizedType()[0].getTypeName());
            var generator = generators.get(ClazzGenerators.forClass(aClass));
            Pattern pattern = Pattern.compile(supplierInput.overrideCoordinate() + "\\[(\\d*)]");
            Optional<Integer> overridedSize = supplierInput.overrideValues().keySet().stream()
                    .filter(s -> pattern.matcher(s).matches())
                    .map(s -> Integer.parseInt(pattern.matcher(s).replaceAll("$1")) + 1)
                    .max(Integer::compareTo);
            final int      listSize      = overridedSize.orElse(new SecureRandom().nextInt(1, 15));
            return IntStream.range(0, listSize)
                    .mapToObj(i -> generator.apply(new ClassDefinition<>(aClass,
                                                                            supplierInput.overrideValues(),
                                                                            supplierInput.overrideCoordinate() + "[%d]".formatted(i))))
                    .collect(Collectors.toSet());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<ClazzGenerators, AbstractGenerator<?>> getGenerator() {
        return generators;
    }

}
