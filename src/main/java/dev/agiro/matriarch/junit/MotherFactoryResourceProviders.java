package dev.agiro.matriarch.junit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.agiro.matriarch.domain.core.ObjectMotherGenerator;
import dev.agiro.matriarch.domain.model.ClassDefinition;
import dev.agiro.matriarch.domain.model.Overrider;
import dev.agiro.matriarch.junit.annotations.MotherFactoryResource;
import dev.agiro.matriarch.junit.annotations.OverrideField;
import dev.agiro.matriarch.junit.annotations.internal.NoSupplier;
import dev.agiro.matriarch.junit.annotations.RandomArg;
import dev.agiro.matriarch.util.OverrideUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.agiro.matriarch.util.OverrideUtils.computeOverrideDefinitions;

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

    private final ObjectMotherGenerator generator = new ObjectMotherGenerator();
    private final ObjectMapper objectMapper = new ObjectMapper();

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
                    argsResource.jsonOverrides(),
                    true
            );

            Object generatedObject = generator.createObject(
                    new ClassDefinition<>(
                            argsResource.targetClass(),
                            overrides,
                            ""
                    )
            );

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


}
