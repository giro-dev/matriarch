package dev.agiro.matriarch.parametrized_test_source;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.agiro.matriarch.domain.core.ObjectMotherGenerator;
import dev.agiro.matriarch.domain.model.ClassProperties;
import dev.agiro.matriarch.parametrized_test_source.annotations.MotherFactoryResource;
import dev.agiro.matriarch.parametrized_test_source.annotations.OverrideField;
import dev.agiro.matriarch.domain.model.Overrider;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MotherFactoryResourceProviders implements ArgumentsProvider, AnnotationConsumer<MotherFactoryResource> {

    private Object[] args;

    private final ObjectMotherGenerator generator    = new ObjectMotherGenerator();
    private final ObjectMapper          objectMapper = new ObjectMapper();


    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Arrays.stream(new Arguments[]{Arguments.arguments(args)});
    }

    @Override
    public void accept(MotherFactoryResource motherFactoryResource) {
        this.args = Stream.of(motherFactoryResource.args())
                .map(argsResource -> generator.createObject(new ClassProperties<>(argsResource.targetClass(),
                                                                                  computeOverrideDefinitions(argsResource.overrides(),
                                                                             argsResource.jsonOverrides()),
                                                                                  "")))
                .toArray();
    }

    private Map<String, Overrider> computeOverrideDefinitions(OverrideField[] overrides,
                                                             String jsonOverrides) {
        final Map<String, Overrider> overrideValues = Arrays.stream(overrides)
                .collect(Collectors.toMap(
                        OverrideField::field,
                        overrideValue -> new Overrider(overrideValue.value(),
                                                       overrideValue.isRegexPattern())));

        try {
            flattenNodes(objectMapper.readTree(jsonOverrides), "", overrideValues);
        } catch (JsonProcessingException e) {
            throw new ParameterResolutionException("Error in Json format : " + jsonOverrides);
        }
        return overrideValues;
    }

    private void flattenNodes(JsonNode node, String currentPath, Map<String, Overrider> flattenedMap) {
        if (node.isValueNode()) {
            flattenedMap.put(currentPath, new Overrider(node.asText(), false));
        } else if (node.isObject()) {

            node.fields().forEachRemaining(entry -> {
                String newPath = currentPath.isEmpty() ? entry.getKey() : currentPath + "." + entry.getKey();
                flattenNodes(entry.getValue(), newPath, flattenedMap);
            });
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                String newPath = currentPath + ".[" + i + "]" ;
                flattenNodes(node.get(i), newPath, flattenedMap);
            }
        }
    }


}
