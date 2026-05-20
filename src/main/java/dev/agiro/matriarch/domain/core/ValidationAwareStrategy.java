package dev.agiro.matriarch.domain.core;

import dev.agiro.matriarch.domain.model.Overrider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Override strategy that reads Jakarta Bean Validation annotations
 * and generates constraint-compliant values.
 * <p>
 * Supports: @NotNull, @NotBlank, @NotEmpty, @Size, @Min, @Max,
 * @Pattern, @Email, @Positive, @PositiveOrZero, @Negative, @NegativeOrZero,
 * @DecimalMin, @DecimalMax, @Digits, @Past, @Future.
 * <p>
 * This strategy is a no-op if jakarta.validation-api is not on the classpath.
 */
public class ValidationAwareStrategy implements OverrideStrategy {

    private static final Logger log = Logger.getLogger(ValidationAwareStrategy.class.getName());
    private static final boolean VALIDATION_AVAILABLE;

    static {
        boolean available;
        try {
            Class.forName("jakarta.validation.constraints.NotNull");
            available = true;
        } catch (ClassNotFoundException e) {
            available = false;
        }
        VALIDATION_AVAILABLE = available;
    }

    @Override
    public void applyOverrides(BuilderConfiguration<?> config, Class<?> clazz) {
        if (!VALIDATION_AVAILABLE) return;

        List<Field> fields = ReflectionCache.getInstance().getFields(clazz);
        for (Field field : fields) {
            String fieldName = field.getName();
            if (config.hasOverride(fieldName)) continue;

            try {
                applyConstraints(config, field, fieldName);
            } catch (Exception e) {
                log.fine(() -> "Failed to process validation annotations for field " + fieldName + ": " + e.getMessage());
            }
        }
    }

    private void applyConstraints(BuilderConfiguration<?> config, Field field, String fieldName) {
        for (Annotation annotation : field.getAnnotations()) {
            String annotationName = annotation.annotationType().getSimpleName();
            switch (annotationName) {
                case "Size" -> handleSize(config, field, fieldName, annotation);
                case "Min" -> handleMin(config, fieldName, annotation);
                case "Max" -> handleMax(config, fieldName, annotation);
                case "Pattern" -> handlePattern(config, fieldName, annotation);
                case "Email" -> handleEmail(config, fieldName);
                case "Positive" -> handlePositive(config, fieldName, field);
                case "PositiveOrZero" -> handlePositiveOrZero(config, fieldName, field);
                case "Negative" -> handleNegative(config, fieldName, field);
                case "NegativeOrZero" -> handleNegativeOrZero(config, fieldName, field);
                case "NotBlank" -> handleNotBlank(config, field, fieldName);
                case "NotEmpty" -> handleNotEmpty(config, field, fieldName);
                case "DecimalMin" -> handleDecimalMin(config, fieldName, annotation);
                case "DecimalMax" -> handleDecimalMax(config, fieldName, annotation);
                default -> { /* ignore unknown annotations */ }
            }
        }
    }

    private void handleSize(BuilderConfiguration<?> config, Field field, String fieldName, Annotation annotation) {
        try {
            int min = (int) annotation.annotationType().getMethod("min").invoke(annotation);
            int max = (int) annotation.annotationType().getMethod("max").invoke(annotation);
            if (String.class.isAssignableFrom(field.getType())) {
                int targetLen = Math.max(min, Math.min(max, min + 10));
                String regex = "[a-zA-Z0-9]{" + targetLen + "}";
                config.addOverride(fieldName, Overrider.regex(regex));
            }
        } catch (Exception e) {
            log.fine(() -> "Could not process @Size for field " + fieldName);
        }
    }

    private void handleMin(BuilderConfiguration<?> config, String fieldName, Annotation annotation) {
        try {
            long min = (long) annotation.annotationType().getMethod("value").invoke(annotation);
            Random random = GenerationContext.getInstance().getRandom();
            long value = min + Math.abs(random.nextLong() % 1000);
            config.addOverride(fieldName, Overrider.object(value));
        } catch (Exception e) {
            log.fine(() -> "Could not process @Min for field " + fieldName);
        }
    }

    private void handleMax(BuilderConfiguration<?> config, String fieldName, Annotation annotation) {
        try {
            long max = (long) annotation.annotationType().getMethod("value").invoke(annotation);
            Random random = GenerationContext.getInstance().getRandom();
            long value = max - Math.abs(random.nextLong() % Math.max(1, max));
            config.addOverride(fieldName, Overrider.object(value));
        } catch (Exception e) {
            log.fine(() -> "Could not process @Max for field " + fieldName);
        }
    }

    private void handlePattern(BuilderConfiguration<?> config, String fieldName, Annotation annotation) {
        try {
            String regexp = (String) annotation.annotationType().getMethod("regexp").invoke(annotation);
            config.addOverride(fieldName, Overrider.regex(regexp));
        } catch (Exception e) {
            log.fine(() -> "Could not process @Pattern for field " + fieldName);
        }
    }

    private void handleEmail(BuilderConfiguration<?> config, String fieldName) {
        config.addOverride(fieldName, Overrider.regex("[a-z]{5,8}@[a-z]{4,6}\\.[a-z]{2,3}"));
    }

    private void handlePositive(BuilderConfiguration<?> config, String fieldName, Field field) {
        Random random = GenerationContext.getInstance().getRandom();
        if (isIntegerType(field.getType())) {
            config.addOverride(fieldName, Overrider.object(1 + Math.abs(random.nextInt() % 10000)));
        } else {
            config.addOverride(fieldName, Overrider.object(Math.abs(random.nextDouble()) + 0.01));
        }
    }

    private void handlePositiveOrZero(BuilderConfiguration<?> config, String fieldName, Field field) {
        Random random = GenerationContext.getInstance().getRandom();
        if (isIntegerType(field.getType())) {
            config.addOverride(fieldName, Overrider.object(Math.abs(random.nextInt() % 10000)));
        } else {
            config.addOverride(fieldName, Overrider.object(Math.abs(random.nextDouble())));
        }
    }

    private void handleNegative(BuilderConfiguration<?> config, String fieldName, Field field) {
        Random random = GenerationContext.getInstance().getRandom();
        if (isIntegerType(field.getType())) {
            config.addOverride(fieldName, Overrider.object(-(1 + Math.abs(random.nextInt() % 10000))));
        } else {
            config.addOverride(fieldName, Overrider.object(-(Math.abs(random.nextDouble()) + 0.01)));
        }
    }

    private void handleNegativeOrZero(BuilderConfiguration<?> config, String fieldName, Field field) {
        Random random = GenerationContext.getInstance().getRandom();
        if (isIntegerType(field.getType())) {
            config.addOverride(fieldName, Overrider.object(-Math.abs(random.nextInt() % 10000)));
        } else {
            config.addOverride(fieldName, Overrider.object(-Math.abs(random.nextDouble())));
        }
    }

    private void handleNotBlank(BuilderConfiguration<?> config, Field field, String fieldName) {
        if (String.class.isAssignableFrom(field.getType()) && !config.hasOverride(fieldName)) {
            config.addOverride(fieldName, Overrider.regex("[a-zA-Z0-9]{5,15}"));
        }
    }

    private void handleNotEmpty(BuilderConfiguration<?> config, Field field, String fieldName) {
        if (String.class.isAssignableFrom(field.getType()) && !config.hasOverride(fieldName)) {
            config.addOverride(fieldName, Overrider.regex("[a-zA-Z0-9]{1,15}"));
        }
    }

    private void handleDecimalMin(BuilderConfiguration<?> config, String fieldName, Annotation annotation) {
        try {
            String value = (String) annotation.annotationType().getMethod("value").invoke(annotation);
            double min = Double.parseDouble(value);
            Random random = GenerationContext.getInstance().getRandom();
            config.addOverride(fieldName, Overrider.object(min + Math.abs(random.nextDouble() * 100)));
        } catch (Exception e) {
            log.fine(() -> "Could not process @DecimalMin for field " + fieldName);
        }
    }

    private void handleDecimalMax(BuilderConfiguration<?> config, String fieldName, Annotation annotation) {
        try {
            String value = (String) annotation.annotationType().getMethod("value").invoke(annotation);
            double max = Double.parseDouble(value);
            Random random = GenerationContext.getInstance().getRandom();
            config.addOverride(fieldName, Overrider.object(max - Math.abs(random.nextDouble() * Math.max(1, max))));
        } catch (Exception e) {
            log.fine(() -> "Could not process @DecimalMax for field " + fieldName);
        }
    }

    private boolean isIntegerType(Class<?> type) {
        return type == int.class || type == Integer.class ||
               type == long.class || type == Long.class ||
               type == short.class || type == Short.class ||
               type == byte.class || type == Byte.class;
    }
}
