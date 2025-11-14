package dev.agiro.matriarch.junit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.agiro.matriarch.domain.core.Mother;
import dev.agiro.matriarch.domain.core.Regex;
import dev.agiro.matriarch.domain.model.Overrider;
import dev.agiro.matriarch.junit.annotations.MotherConfig;
import dev.agiro.matriarch.junit.annotations.OverrideField;
import dev.agiro.matriarch.junit.annotations.internal.NoSupplier;
import dev.agiro.matriarch.util.OverrideUtils;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static dev.agiro.matriarch.util.OverrideUtils.computeOverrideDefinitions;

/**
 * JUnit 5 extension that generates instances for fields/parameters annotated with @Mother.
 */
public class MotherExtension implements TestInstancePostProcessor, ParameterResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        Class<?> testClass = testInstance.getClass();
        MotherConfig cfg = testClass.getAnnotation(MotherConfig.class);

        for (Field field : testClass.getDeclaredFields()) {
            dev.agiro.matriarch.junit.annotations.Mother motherAnn = field.getAnnotation(dev.agiro.matriarch.junit.annotations.Mother.class);
            if (motherAnn == null) continue;

            Object value = buildFor(field.getType(), motherAnn, cfg);
            boolean access = field.canAccess(testInstance);
            field.setAccessible(true);
            field.set(testInstance, value);
            field.setAccessible(access);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(dev.agiro.matriarch.junit.annotations.Mother.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> type = parameterContext.getParameter().getType();
        dev.agiro.matriarch.junit.annotations.Mother motherAnn = parameterContext.findAnnotation(dev.agiro.matriarch.junit.annotations.Mother.class).orElse(null);
        MotherConfig cfg = extensionContext.getRequiredTestClass().getAnnotation(MotherConfig.class);
        if (motherAnn == null) {
            throw new ParameterResolutionException("@Mother annotation missing on parameter");
        }
        try {
            return buildFor(type, motherAnn, cfg);
        } catch (Exception e) {
            throw new ParameterResolutionException("Failed to resolve @Mother parameter: " + e.getMessage(), e);
        }
    }

    private Object buildFor(Class<?> type, dev.agiro.matriarch.junit.annotations.Mother motherAnn, MotherConfig cfg) {
        Mother.Builder<?> b = Mother.forClass(type);
        if (cfg != null) {
            b.withCollectionSize(cfg.collectionMin(), cfg.collectionMax());
        }
        var jsonWins = cfg == null || cfg.jsonOverridesWin();
        Map<String, Overrider> overrides = computeOverrideDefinitions(motherAnn.overrides(),
                motherAnn.jsonOverrides(),
                jsonWins);
        // Apply computed overrides via builder
        overrides.forEach((k, v) -> {
            switch (v.type()) {
                case STRING -> b.forField(k, (String) v.value());
                case REGEX -> b.forField(k, new Regex((String) v.value()));
                case OBJECT, NULL -> b.forField(k, v.value());
                case SUPPLIER -> b.forField(k, (java.util.function.Supplier<?>) v.value());
            }
        });
        return b.build();
    }


}
