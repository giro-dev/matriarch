package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithMixedFinalAndNonFinalFields {
    private final int code;
    private String name;

    public ClassWithMixedFinalAndNonFinalFields(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
