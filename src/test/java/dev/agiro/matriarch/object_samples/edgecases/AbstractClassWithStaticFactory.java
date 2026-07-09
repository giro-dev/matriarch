package dev.agiro.matriarch.object_samples.edgecases;

public abstract class AbstractClassWithStaticFactory {
    private final String name;

    protected AbstractClassWithStaticFactory(String name) {
        this.name = name;
    }

    public static AbstractClassWithStaticFactory create() {
        return new ConcreteAbstractImpl("abstract-factory");
    }

    public String getName() {
        return name;
    }
}
