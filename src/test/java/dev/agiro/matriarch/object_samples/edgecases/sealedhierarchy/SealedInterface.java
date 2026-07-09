package dev.agiro.matriarch.object_samples.edgecases.sealedhierarchy;

public sealed interface SealedInterface permits SealedInterfaceRecordA, SealedInterfaceRecordB {
    String label();

    static SealedInterface create() {
        return new SealedInterfaceRecordA("factory", 99);
    }
}

record SealedInterfaceRecordA(String label, int count) implements SealedInterface {
}

record SealedInterfaceRecordB(String label, int count) implements SealedInterface {
}
