package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithPrivateConstructorAndParamStaticFactory {
    private String name;

    private ClassWithPrivateConstructorAndParamStaticFactory(String name) {
        this.name = name;
    }

    public static ClassWithPrivateConstructorAndParamStaticFactory create(String name) {
        return new ClassWithPrivateConstructorAndParamStaticFactory(name);
    }

    public String getName() {
        return name;
    }
}
