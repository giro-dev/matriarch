package dev.agiro.matriarch.object_samples.edgecases;

public final class ClassWithAllArgsConstructorAndNoDefault {
    private final String name;
    private final int count;

    public ClassWithAllArgsConstructorAndNoDefault(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
}
