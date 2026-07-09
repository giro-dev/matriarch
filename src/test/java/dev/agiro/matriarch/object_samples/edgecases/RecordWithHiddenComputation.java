package dev.agiro.matriarch.object_samples.edgecases;

public record RecordWithHiddenComputation(String raw, int value) {
    public RecordWithHiddenComputation {
        raw = raw.toUpperCase();
        value = value * 2;
    }
}
