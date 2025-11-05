package dev.agiro.matriarch.parametrized_test_source;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.agiro.matriarch.domain.core.ObjectMotherGenerator;
import dev.agiro.matriarch.domain.model.ClassDefinition;
import dev.agiro.matriarch.domain.model.Overrider;
import dev.agiro.matriarch.parametrized_test_source.annotations.MotherFactoryResource;
import dev.agiro.matriarch.parametrized_test_source.annotations.OverrideField;
import dev.agiro.matriarch.parametrized_test_source.annotations.RandomArg;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ArgumentsProvider for JUnit parameterized tests that generates test objects using Matriarch's ObjectMother.
 *
 * <p>Example usage:
 * <pre>{@code
 * @ParameterizedTest
 * @MotherFactoryResource(args = {
 *     @RandomArg(
 *         name = "Valid User",
 *         targetClass = User.class,
 *         overrides = {
 *             @OverrideField(field = "email", value = "test@example.com"),
 *             @OverrideField(field = "age", value = "25", type = Overrider.OverriderType.OBJECT)
 *         }
 *     ),
 *     @RandomArg(
 *         name = "Admin User",
 *         targetClass = User.class,
 *         jsonOverrides = """
 *             {
 *                 "email": "admin@example.com",
 *                 "role": "ADMIN"
 *             }
 *             """
 *     )
 * })
 * void testUserCreation(User user) {
 *     assertNotNull(user);
 * }
 * }</pre>
 */
public class MotherFactoryResourceProviders implements ArgumentsProvider, AnnotationConsumer<MotherFactoryResource> {

    private Arguments[] arguments;

    private final ObjectMotherGenerator generator    = new ObjectMotherGenerator();
    private final ObjectMapper          objectMapper = new ObjectMapper();

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        if (arguments == null || arguments.length == 0) {
            throw new ParameterResolutionException(
                "No arguments configured. Please provide at least one @RandomArg in @MotherFactoryResource");
        }
        return Arrays.stream(arguments);
    }

    @Override
    public void accept(MotherFactoryResource motherFactoryResource) {
        RandomArg[] randomArgs = motherFactoryResource.args();

        if (randomArgs == null || randomArgs.length == 0) {
            throw new ParameterResolutionException(
                "@MotherFactoryResource must have at least one @RandomArg defined");
        }

        this.arguments = Arrays.stream(randomArgs)
                .map(this::createArgumentForRandomArg)
                .toArray(Arguments[]::new);
    }

    /**
     * Creates a named Arguments object from a RandomArg configuration.
     */
    private Arguments createArgumentForRandomArg(RandomArg argsResource) {
        try {
            Map<String, Overrider> overrides = computeOverrideDefinitions(
                argsResource.overrides(),
                argsResource.jsonOverrides()
            );

            Object generatedObject = generator.createObject(
                new ClassDefinition<>(
                    argsResource.targetClass(),
                    overrides,
                    argsResource.name().isEmpty() ? argsResource.targetClass().getSimpleName() : argsResource.name()
                )
            );

            if (!argsResource.name().isEmpty()) {
                return Arguments.of(generatedObject);
            }

            return Arguments.of(generatedObject);

        } catch (Exception e) {
            String testCaseName = argsResource.name().isEmpty()
                ? argsResource.targetClass().getSimpleName()
                : argsResource.name();
            throw new ParameterResolutionException(
                "Failed to generate test object for test case '" + testCaseName + "': " + e.getMessage(),
                e
            );
        }
    }

    /**
     * Computes override definitions from both array-based and JSON-based overrides.
     * JSON overrides take precedence over array-based overrides for the same field.
     */
    private Map<String, Overrider> computeOverrideDefinitions(OverrideField[] overrides,
                                                             String jsonOverrides) {
        final Map<String, Overrider> overrideValues = Arrays.stream(overrides)
                .collect(Collectors.toMap(
                        OverrideField::field,
                        overrideField -> {
                            // If isRegex is true, use REGEX type regardless of specified type
                            Overrider.OverriderType type = overrideField.isRegex()
                                ? Overrider.OverriderType.REGEX
                                : overrideField.type();
                            return new Overrider(overrideField.value(), type);
                        }));

        if (jsonOverrides != null && !jsonOverrides.trim().isEmpty()) {
            try {
                flattenNodes(objectMapper.readTree(jsonOverrides), "", overrideValues);
            } catch (JsonProcessingException e) {
                throw new ParameterResolutionException(
                    "Invalid JSON format in jsonOverrides: " + e.getMessage() +
                    "\nJSON provided: " + jsonOverrides,
                    e
                );
            }
        }

        return overrideValues;
    }

    private void flattenNodes(JsonNode node, String currentPath, Map<String, Overrider> flattenedMap) {
        if (node.isValueNode()) {
            var type = node.isTextual() ? Overrider.OverriderType.STRING : Overrider.OverriderType.OBJECT;
            flattenedMap.put(currentPath, new Overrider(node.asText(), type));
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
