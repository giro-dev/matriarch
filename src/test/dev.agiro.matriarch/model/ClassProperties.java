package dev.agiro.matriarch.model;


import java.util.Map;

public record ClassProperties<T>(Class<T> fieldType,
                              Map<String, Overrider> overrideValues,
                              String overrideCoordinate) {
}
