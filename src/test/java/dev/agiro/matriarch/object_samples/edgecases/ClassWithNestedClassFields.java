package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithNestedClassFields {
    private Nested nested;

    public Nested getNested() {
        return nested;
    }

    public static class Nested {
        private String value;

        public Nested() {
        }

        public String getValue() {
            return value;
        }
    }
}
