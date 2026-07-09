package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithAllFinalFields {
    private final String name;
    private final int code;

    public ClassWithAllFinalFields(String name, int code) {
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
