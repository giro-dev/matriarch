package dev.agiro.matriarch.object_samples.staticfactory;

public class PrivateConstructorPublicStaticFactory {
    private String id;
    private String value;

    private PrivateConstructorPublicStaticFactory(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public static PrivateConstructorPublicStaticFactory createDefault() {
        return new PrivateConstructorPublicStaticFactory("defaultId", "defaultValue");
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
