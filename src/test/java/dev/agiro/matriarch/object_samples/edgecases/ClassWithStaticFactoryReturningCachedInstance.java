package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithStaticFactoryReturningCachedInstance {
    private static final ClassWithStaticFactoryReturningCachedInstance INSTANCE = new ClassWithStaticFactoryReturningCachedInstance();

    private String name = "cached";

    private ClassWithStaticFactoryReturningCachedInstance() {
    }

    public static ClassWithStaticFactoryReturningCachedInstance getInstance() {
        return INSTANCE;
    }

    public String getName() {
        return name;
    }
}
