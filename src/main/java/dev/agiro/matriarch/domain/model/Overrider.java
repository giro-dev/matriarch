package dev.agiro.matriarch.domain.model;

public record Overrider(Object value, OverriderType type) {
    public static Overrider with(String value) {
        return new Overrider(value, OverriderType.STRING);
    }

    public static Overrider regex(String value) {
        return new Overrider(value, OverriderType.REGEX);
    }

    public static Overrider object(Object object) {
        return new Overrider(object, OverriderType.OBJECT);
    }

    public static Overrider nullValue() {
        return new Overrider(null, OverriderType.NULL);
    }

    public enum OverriderType {NULL, STRING, REGEX, OBJECT}
}

