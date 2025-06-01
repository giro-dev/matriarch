package dev.agiro.matriarch.domain.core.generators;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;


public enum ClazzGenerators {
    STRING(StringGenerator.class, List.of(String.class)),
    CHARACTER(CharacterGenerator.class, List.of(char.class, Character.class)),
    BOOLEAN(BooleanGenerator.class, List.of(boolean.class, Boolean.class)),
    INTEGER(IntegerGenerator.class, List.of(int.class, Integer.class)),
    LONG(LongGenerator.class, List.of(long.class, Long.class)),
    FLOAT(FloatGenerator.class, List.of(float.class, Float.class)),
    DOUBLE(DoubleGenerator.class, List.of(double.class, Double.class)),
    INSTANT(InstantGenerator.class, List.of(Instant.class)),
    TIMESTAMP(TimestampGenerator.class, List.of(Timestamp.class)),
    //DATETIME_FORMATER(DateTimeFormatterGenerator.class, List.of(DateTimeFormatter.class)),
    UUID(UUIDGenerator.class, List.of(java.util.UUID.class)),
    BIG_DECIMAL(BigDecimalGenerator.class, List.of(BigDecimal.class)),
    DATE(LocalDateGenerator.class, List.of(LocalDate.class)),
    //ARRAY(ArrayGenerator.class, List.of(Array.class)),
    LIST(ListGenerator.class, List.of(List.class)),
    SET(SetGenerator.class, List.of(Set.class)),
    MAP(MapGenerator.class, List.of(Map.class)),
    ENUM(EnumGenerator.class, List.of()),
    GENERIC(GenericObjectGenerator.class, List.of());

    private final Class<? extends AbstractGenerator<?>> generator;
    private final List<Class<?>> clazz;

    ClazzGenerators(Class<? extends AbstractGenerator<?>> generator,List<Class<?>> clazz){
        this.generator = generator;
        this.clazz     = clazz;
    }

    public static ClazzGenerators forClass(Class<?> clazz){
        // Default behavior if no type arguments are available or relevant
        return forClass(clazz, null);
    }

    //This was the duplicate method causing the compilation error.
    //public static ClazzGenerators forClass(Class<?> clazz){
    //    // Default behavior if no type arguments are available or relevant
    //    return forClass(clazz, null);
    //}

    public static ClazzGenerators forClass(Class<?> rawType, java.lang.reflect.Type[] actualTypeArguments) {
        // If the rawType is Number itself and we have a concrete type argument for it (e.g. field like NumberBox<Double>)
        if (Number.class.isAssignableFrom(rawType) && actualTypeArguments != null && actualTypeArguments.length > 0) {
            if (actualTypeArguments[0] instanceof Class) {
                Class<?> concreteTypeArgument = (Class<?>) actualTypeArguments[0];
                if (Number.class.isAssignableFrom(concreteTypeArgument)) { // Check if the argument is a Number subclass
                    ClazzGenerators specificGenerator = Arrays.stream(values())
                            .filter(type -> type.clazz.contains(concreteTypeArgument))
                            .findFirst()
                            .orElse(null);
                    if (specificGenerator != null) {
                        return specificGenerator; // Use generator for Double, Integer, etc.
                    }
                }
            }
        }

        // If rawType is Number.class directly (e.g. field `Number myNumber;` or unresolved generic like N from `N numberContent`)
        // and we couldn't resolve it to a specific number type from type arguments, default to Double.
        if (rawType.equals(Number.class)) {
            return DOUBLE; // Default to DoubleGenerator for a raw Number field
        }

        // Fallback to existing logic for other types or if no specific generic type generator is found
        return Arrays.stream(values())
                .filter(type -> type.clazz.contains(rawType))
                .findFirst()
                .orElse(rawType.isEnum() ? ENUM : GENERIC);
    }

    public Class<? extends AbstractGenerator<?>> getGenerator() {
        return generator;
    }
}
