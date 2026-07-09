package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithProtectedConstructor {
    private String label;
    private int count;

    protected ClassWithProtectedConstructor(String label, int count) {
        this.label = label;
        this.count = count;
    }

    public String getLabel() {
        return label;
    }

    public int getCount() {
        return count;
    }
}
