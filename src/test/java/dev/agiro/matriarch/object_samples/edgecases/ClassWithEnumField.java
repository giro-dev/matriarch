package dev.agiro.matriarch.object_samples.edgecases;

public class ClassWithEnumField {
    private Status status;

    public Status getStatus() {
        return status;
    }

    public enum Status {
        ACTIVE, INACTIVE, PENDING
    }
}
