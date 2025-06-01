package dev.agiro.matriarch.object_samples;

public class SimplePrivateConstructor {
    private String data;

    private SimplePrivateConstructor(String data) {
        this.data = data;
    }

    public static SimplePrivateConstructor create(String data) {
        return new SimplePrivateConstructor(data);
    }

    public String getData() {
        return data;
    }
}
