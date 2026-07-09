package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithMultipleStaticFactories {
    private String configName;
    private int version;

    private ClassWithMultipleStaticFactories(String configName, int version) {
        this.configName = configName;
        this.version = version;
    }

    public static ClassWithMultipleStaticFactories createDefault() {
        return new ClassWithMultipleStaticFactories("default", 0);
    }

    public static ClassWithMultipleStaticFactories create(String configName, int version) {
        return new ClassWithMultipleStaticFactories(configName, version);
    }

    public String getConfigName() {
        return configName;
    }

    public int getVersion() {
        return version;
    }
}
