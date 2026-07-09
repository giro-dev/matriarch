package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithStaticFactoryThatThrows {
    private final String origin;

    public ClassWithStaticFactoryThatThrows() {
        this.origin = "constructor";
    }

    public static ClassWithStaticFactoryThatThrows create() {
        throw new IllegalStateException("Factory failed");
    }

    public String getOrigin() {
        return origin;
    }
}
