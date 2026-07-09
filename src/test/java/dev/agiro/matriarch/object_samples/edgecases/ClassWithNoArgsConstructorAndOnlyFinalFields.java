package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithNoArgsConstructorAndOnlyFinalFields {
    private final String name;
    private final int count;

    public ClassWithNoArgsConstructorAndOnlyFinalFields() {
        this.name = "default";
        this.count = 0;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
}
