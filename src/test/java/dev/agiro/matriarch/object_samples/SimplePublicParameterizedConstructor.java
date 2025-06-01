package dev.agiro.matriarch.object_samples;

public class SimplePublicParameterizedConstructor {
    private String name;
    private int value;

    public SimplePublicParameterizedConstructor(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
