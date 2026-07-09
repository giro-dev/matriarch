package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithBuilderPattern {
    private final String name;

    private ClassWithBuilderPattern(String name) {
        this.name = name;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public static class Builder {
        public Builder name(String name) {
            return this;
        }

        public ClassWithBuilderPattern build() {
            return new ClassWithBuilderPattern("builder");
        }
    }
}
