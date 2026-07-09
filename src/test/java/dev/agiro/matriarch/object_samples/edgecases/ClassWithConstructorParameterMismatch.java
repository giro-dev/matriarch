package dev.agiro.matriarch.object_samples.edgecases;

public final class ClassWithConstructorParameterMismatch {
    private final String value;

    public ClassWithConstructorParameterMismatch(String data) {
        this.value = data.toUpperCase();
    }

    public String getValue() {
        return value;
    }
}
