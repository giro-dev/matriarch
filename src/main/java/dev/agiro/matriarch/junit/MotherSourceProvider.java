package dev.agiro.matriarch.junit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.agiro.matriarch.domain.core.ObjectMotherGenerator;
import dev.agiro.matriarch.domain.model.Overrider;
import dev.agiro.matriarch.junit.annotations.MotherSource;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.*;
import java.util.stream.Stream;

import static dev.agiro.matriarch.util.OverrideUtils.computeOverrideDefinitions;

public class MotherSourceProvider implements ArgumentsProvider {
    private final ObjectMotherGenerator generator = new ObjectMotherGenerator();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        MotherSource src = context.getRequiredTestMethod().getAnnotation(MotherSource.class);
        if (src == null) {
            throw new ParameterResolutionException("@MotherSource not found on method");
        }
        Map<String, Overrider> overrides = computeOverrideDefinitions(src.overrides(), src.jsonOverrides(), true);
        int count = Math.max(1, src.count());
        Class<?> target = src.targetClass();
        return Stream.iterate(0, i -> i + 1)
                .limit(count)
                .map(i -> {
                    Object obj = generator.createObject(new dev.agiro.matriarch.domain.model.ClassDefinition<>(target, overrides, ""));
                    return Arguments.of(obj);
                });
    }


}
