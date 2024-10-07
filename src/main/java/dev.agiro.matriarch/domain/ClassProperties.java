package dev.agiro.matriarch.domain;


import java.util.Map;

public record ClassProperties<T>(Class<T> fieldType,
                              Map<String, Overrider> overrideValues,
                              String overrideCoordinate) {
}
