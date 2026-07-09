package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithPackagePrivateStaticFactory {
    private final String source;

    public ClassWithPackagePrivateStaticFactory() {
        this.source = "constructor";
    }

    static ClassWithPackagePrivateStaticFactory create() {
        return new ClassWithPackagePrivateStaticFactory();
    }

    public String getSource() {
        return source;
    }
}
