package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithConstructorThatThrowsOnNull {
    private final String label;

    public ClassWithConstructorThatThrowsOnNull(String label) {
        if (label == null) {
            throw new IllegalArgumentException("Label cannot be null");
        }
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
