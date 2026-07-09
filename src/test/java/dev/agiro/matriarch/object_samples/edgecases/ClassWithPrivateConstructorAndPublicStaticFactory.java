package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithPrivateConstructorAndPublicStaticFactory {
    private String name;
    private int version;

    private ClassWithPrivateConstructorAndPublicStaticFactory(String name, int version) {
        this.name = name;
        this.version = version;
    }

    public static ClassWithPrivateConstructorAndPublicStaticFactory create() {
        return new ClassWithPrivateConstructorAndPublicStaticFactory("factory", 1);
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }
}
