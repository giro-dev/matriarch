package dev.agiro.matriarch.domain.model;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum FieldSupplierType {
    STRING(String.class),
    CHARACTER(char.class, Character.class),
    BOOLEAN(boolean.class, Boolean.class),
    INTEGER(int.class, Integer.class),
    LONG(long.class, Long.class),
    FLOAT(float.class, Float.class),
    DOUBLE(double.class, Double.class),
    INSTANT(Instant.class),
    TIMESTAMP(Timestamp.class),
    DATETIME_FORMATER(DateTimeFormatter.class),
    UUID(java.util.UUID.class),
    BIG_DECIMAL(BigDecimal.class),
    DATE(LocalDate.class),
    ARRAY(Array.class),
    LIST(List.class),
    SET(Set.class),
    MAP(Map.class),
    ENUM(),
    UNKNOWN();

    private final List<Class<?>> clazz;

    FieldSupplierType(Class<?>... clazz){
        this.clazz = Arrays.stream(clazz).toList();
    }

    public static FieldSupplierType forClass(Class<?> clazz){
        return Arrays.stream(values())
                .filter(type -> type.clazz.contains(clazz))
                .findFirst()
                .orElse(clazz.isEnum() ? ENUM : UNKNOWN);
    }

}
