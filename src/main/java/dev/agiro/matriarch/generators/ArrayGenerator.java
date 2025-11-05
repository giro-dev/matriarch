package dev.agiro.matriarch.generators;



import dev.agiro.matriarch.domain.model.ClassDefinition;
import dev.agiro.matriarch.domain.model.Definition;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class ArrayGenerator extends AbstractGenerator<Object[]> implements MultiGenerator {

    private final Map<ClazzGenerators, AbstractGenerator<?>> generators;

    @SuppressWarnings("unchecked")
    public ArrayGenerator(Map<ClazzGenerators, AbstractGenerator<?>> generators) {
        super( Object[].class);
        this.generators = generators;
    }


    @Override
    public Object[] generate(Definition supplierInput) {
            final Class<?> aClass = supplierInput.clazz().getComponentType();
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
                    .toArray();

    }

    @Override
    public Map<ClazzGenerators, AbstractGenerator<?>> getGenerator() {
        return generators;
    }

}
