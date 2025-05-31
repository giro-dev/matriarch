package dev.agiro.matriarch.object_samples.staticfactory;

public class PrivateConstructorMultipleStaticFactories {
    private String configName;
    private int version;
    private boolean active;

    private PrivateConstructorMultipleStaticFactories(String configName, int version, boolean active) {
        this.configName = configName;
        this.version = version;
        this.active = active;
    }

    public static PrivateConstructorMultipleStaticFactories createActive(String name) {
        return new PrivateConstructorMultipleStaticFactories(name, 1, true);
    }

    public static PrivateConstructorMultipleStaticFactories createInactive(String name, int version) {
        return new PrivateConstructorMultipleStaticFactories(name, version, false);
    }

    public static PrivateConstructorMultipleStaticFactories createDefault() {
        return new PrivateConstructorMultipleStaticFactories("default", 0, false);
    }

    public String getConfigName() {
        return configName;
    }

    public int getVersion() {
        return version;
    }

    public boolean isActive() {
        return active;
    }
}
