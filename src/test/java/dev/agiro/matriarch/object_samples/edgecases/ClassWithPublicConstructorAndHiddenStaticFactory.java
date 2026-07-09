package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithPublicConstructorAndHiddenStaticFactory {
    private final String source;

    public ClassWithPublicConstructorAndHiddenStaticFactory() {
        this.source = "constructor";
    }

    private static ClassWithPublicConstructorAndHiddenStaticFactory create() {
        return new ClassWithPublicConstructorAndHiddenStaticFactory();
    }

    public String getSource() {
        return source;
    }
}
