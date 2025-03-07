package dev.agiro.matriarch.domain.core.generators;



import dev.agiro.matriarch.domain.model.ClassDefinition;
import dev.agiro.matriarch.domain.model.Definition;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ListGenerator extends AbstractGenerator<List<?>> implements MultiGenerator {

    private final Map<ClazzGenerators, AbstractGenerator<?>> generators;

    @SuppressWarnings("unchecked")
    public ListGenerator(Map<ClazzGenerators, AbstractGenerator<?>> generators) {
        super( (Class<List<?>>) (Class<?>) List.class);
        this.generators = generators;
    }


    @Override
    public List<?> generate(Definition supplierInput) {
        try {
            final Class<?> aClass = Class.forName(supplierInput.parametrizedType()[0].getTypeName());
            var generator = generators.get(ClazzGenerators.forClass(aClass));
            final int      listSize      = new SecureRandom().nextInt(1, 15);
            return IntStream.range(0, listSize)
                    .mapToObj(i -> generator.generate(new ClassDefinition<>(aClass,
                                                                            supplierInput.overrideValues(),
                                                                            supplierInput.overrideCoordinate() + "[%d]".formatted(i))))
                    .toList();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<ClazzGenerators, AbstractGenerator<?>> getGenerators() {
        return generators;
    }

}
