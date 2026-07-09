package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithPrivateConstructorAndNoStaticFactory {
    private String name;

    private ClassWithPrivateConstructorAndNoStaticFactory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
