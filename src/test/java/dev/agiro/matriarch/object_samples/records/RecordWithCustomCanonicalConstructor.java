package dev.agiro.matriarch.object_samples.records;

public record RecordWithCustomCanonicalConstructor(String name, int value) {
    public RecordWithCustomCanonicalConstructor(String name, int value) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        this.name = name.toUpperCase();
        this.value = value * 2;
    }
}
