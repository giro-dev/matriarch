package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithGeneric<T> {
    private T value;

    public ClassWithGeneric() {
    }

    public ClassWithGeneric(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
