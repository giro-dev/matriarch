package dev.agiro.matriarch.object_samples.staticfactory;

public class PrivateConstructorStaticFactoryWithParams {
    private String prefix;
    private int number;
    private String suffix;

    private PrivateConstructorStaticFactoryWithParams(String prefix, int number, String suffix) {
        this.prefix = prefix;
        this.number = number;
        this.suffix = suffix;
    }

    public static PrivateConstructorStaticFactoryWithParams create(String prefix, int number, String suffix) {
        if (prefix == null || suffix == null) {
            throw new IllegalArgumentException("Prefix and suffix cannot be null");
        }
        return new PrivateConstructorStaticFactoryWithParams(prefix, number, suffix);
    }

    public String getPrefix() {
        return prefix;
    }

    public int getNumber() {
        return number;
    }

    public String getSuffix() {
        return suffix;
    }
}
