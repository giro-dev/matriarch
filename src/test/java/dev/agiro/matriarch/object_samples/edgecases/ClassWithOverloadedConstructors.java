package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithOverloadedConstructors {
    private String name;
    private int code;

    public ClassWithOverloadedConstructors(String name) {
        this.name = name;
    }

    public ClassWithOverloadedConstructors(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}
