package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithStaticFactoryReturningSubclass {
    protected String name;

    public String getName() {
        return name;
    }

    public static ClassWithStaticFactoryReturningSubclass create() {
        return new SubClass();
    }
}

final class SubClass extends ClassWithStaticFactoryReturningSubclass {
    SubClass() {
        this.name = "subclass";
    }
}
