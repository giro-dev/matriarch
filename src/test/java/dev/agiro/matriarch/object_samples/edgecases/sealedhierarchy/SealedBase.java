package dev.agiro.matriarch.object_samples.edgecases.sealedhierarchy;

public sealed class SealedBase permits SealedChildA, SealedChildB {
    private String type;
    private int value;

    public SealedBase() {
        this.type = "base";
        this.value = 0;
    }

    protected SealedBase(String type, int value) {
        this.type = type;
        this.value = value;
    }

    public static SealedBase create() {
        return new SealedChildA("factory", 42);
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}

final class SealedChildA extends SealedBase {
    public SealedChildA(String type, int value) {
        super(type, value);
    }
}

final class SealedChildB extends SealedBase {
    public SealedChildB(String type, int value) {
        super(type, value);
    }
}
