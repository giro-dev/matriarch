package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithStaticFactoryAndDefaultConstructor {
    private String name;
    private int value;

    public ClassWithStaticFactoryAndDefaultConstructor() {
        this.name = "constructor";
        this.value = -1;
    }

    private ClassWithStaticFactoryAndDefaultConstructor(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static ClassWithStaticFactoryAndDefaultConstructor create() {
        return new ClassWithStaticFactoryAndDefaultConstructor("factory", 42);
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
