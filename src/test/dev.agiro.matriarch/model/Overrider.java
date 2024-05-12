package dev.agiro.matriarch.model;

public record Overrider(String value, boolean isRegexPattern) {
    public static Overrider with(String value) {
        return new Overrider(value, false);
    }
    public static Overrider regex(String value) {
        return new Overrider(value, true);
    }
}

